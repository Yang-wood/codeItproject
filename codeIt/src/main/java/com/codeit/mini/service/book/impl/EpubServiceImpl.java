package com.codeit.mini.service.book.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.service.book.EpubService;

import lombok.extern.log4j.Log4j2;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

@Service
@Log4j2
public class EpubServiceImpl implements EpubService {

    @Override
    public BookDTO bookDTO(File epubfile) throws IOException {
        BookDTO bookDTO = new BookDTO();

        // File 객체에서 InputStream 획득
        try (InputStream epubInputStream = Files.newInputStream(epubfile.toPath())) {
            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpub(epubInputStream);

            // 1. epublib의 Metadata 객체 획득
            Metadata metadata = book.getMetadata();

            // 2. Metadata 객체에서 정보 추출하여 DTO에 매핑
            // - getTitles(): List<String> 반환. 첫 번째 제목 가져오기.
            bookDTO.setTitle(Optional.ofNullable(metadata.getTitles())
                               .filter(list -> !list.isEmpty())
                               .map(list -> list.get(0))
                               .orElse("제목 없음"));

            // - getAuthors(): List<Author> 반환. 첫 번째 작가의 이름 조합.
            if (!metadata.getAuthors().isEmpty()) {
                Author author = metadata.getAuthors().get(0);
                String firstName = Optional.ofNullable(author.getFirstname()).orElse("").trim();
                String lastName = Optional.ofNullable(author.getLastname()).orElse("").trim();

                String authorName;
                if (!firstName.isEmpty() && !lastName.isEmpty()) {
                    authorName = firstName + " " + lastName;
                } else if (!firstName.isEmpty()) {
                    authorName = firstName;
                } else if (!lastName.isEmpty()) {
                    authorName = lastName;
                } else {
                    authorName = "작가 미상";
                }
                bookDTO.setAuthor(authorName.trim());
            } else {
                bookDTO.setAuthor("작가 미상");
            }

            // - getPublishers(): List<String> 반환. 첫 번째 출판사 가져오기.
            bookDTO.setPublisher(Optional.ofNullable(metadata.getPublishers())
                                 .filter(list -> !list.isEmpty())
                                 .map(list -> list.get(0))
                                 .orElse("출판사 미상"));

            // - getDates(): List<Date> 반환. 첫 번째 날짜의 String 값 가져오기.
            // EPUB 내부의 날짜 형식은 다양할 수 있어 일단 String으로 받음.
            bookDTO.setPubDate(Optional.ofNullable(metadata.getDates())
                               .filter(list -> !list.isEmpty())
                               .map(list -> list.get(0).getValue()) // Date 객체에서 String 값 추출
                               .orElse("날짜 미상"));

            // - getSubjects(): List<String> 반환. 여러 주제를 콤마로 연결.
            bookDTO.setCategory(Optional.ofNullable(metadata.getSubjects())
                                .filter(list -> !list.isEmpty())
                                .map(list -> String.join(", ", list))
                                .orElse("카테고리 없음"));

            // - getDescriptions(): List<String> 반환. 첫 번째 설명 가져오기.
            bookDTO.setDescription(Optional.ofNullable(metadata.getDescriptions())
                                   .filter(list -> !list.isEmpty())
                                   .map(list -> list.get(0))
                                   .orElse("설명 없음"));


            // --- 표지 이미지 추출 로직 ---
            byte[] imageData = null;
            String mimeType = null;
            Resource coverResource = null; // 실제 이미지 리소스를 저장할 변수

            // 1. book.getCoverPage()가 직접 이미지 리소스를 반환하는지 시도
            Resource potentialCoverPage = book.getCoverPage();
            if (potentialCoverPage != null &&
                (potentialCoverPage.getMediaType().equals(MediatypeService.JPG) ||
                 potentialCoverPage.getMediaType().equals(MediatypeService.PNG) ||
                 potentialCoverPage.getMediaType().equals(MediatypeService.GIF))) {

                coverResource = potentialCoverPage;

            } else {
                // 2. getCoverPage()가 이미지가 아니거나 null인 경우, 명시적으로 흔한 경로들을 찾아 시도
                // images/cover.jpg
                coverResource = book.getResources().getByHref("images/cover.jpg");
                if (coverResource == null) {
                    // OEBPS/images/cover.jpg
                    coverResource = book.getResources().getByHref("OEBPS/images/cover.jpg");
                }
                if (coverResource == null) {
                    // cover.jpg (루트 폴더)
                    coverResource = book.getResources().getByHref("cover.jpg");
                }
                if (coverResource == null) {
                    // 마지막 수단: 책에서 첫 번째로 발견되는 이미지 (표지 이미지가 아닐 수 있음)
                    for (Resource res : book.getResources().getAll()) {
                        if (res.getMediaType() != null &&
                            (res.getMediaType().equals(MediatypeService.JPG) ||
                             res.getMediaType().equals(MediatypeService.PNG) ||
                             res.getMediaType().equals(MediatypeService.GIF))) {
                            coverResource = res;
                            break; // 첫 번째 이미지만 찾으면 중단
                        }
                    }
                }

                if (coverResource != null) {
                	log.info("Found cover image by direct Href lookup or fallback: {}", coverResource.getHref());
                } else {
                	log.info("No direct image resource found via common Hrefs or fallback.");
                }
            }

            // 실제 이미지 리소스가 찾아졌다면 데이터를 추출하고 Base64 인코딩
            if (coverResource != null) {
                imageData = coverResource.getData(); // Resource에서 byte[] 데이터 가져오기

                if (imageData != null && imageData.length > 0) {
                    bookDTO.setCoverImageData(imageData); // 원본 byte[] 데이터를 DTO에 저장

                    String base64Image = Base64.getEncoder().encodeToString(imageData);

                    // MIME 타입도 정확히 가져와서 포함 (없으면 기본값 "image/jpeg")
                    mimeType = coverResource.getMediaType() != null ?
                               coverResource.getMediaType().getName() : "image/jpeg";
                    bookDTO.setCoverImageDataBase64("data:" + mimeType + ";base64," + base64Image);
                    log.info("Final Base64 Image Length: {}", base64Image.length());
                    log.info("Base64 Image starts with: {}", base64Image.substring(0, Math.min(base64Image.length(), 50)));
                    log.info("Final Image MIME Type: {}", mimeType);
                } else {
                	log.info("Image data from coverResource is null or empty.");
                    bookDTO.setCoverImageData(null);
                    bookDTO.setCoverImageDataBase64(null); // 명시적으로 null 설정
                }
            } else {
                bookDTO.setCoverImageData(null);
                bookDTO.setCoverImageDataBase64(null); // 명시적으로 null 설정
            }
            // --- 표지 이미지 추출 끝 ---

            return bookDTO;

        } catch (IOException e) {
            // EPUB 파일 읽기 중 오류 발생 시 예외 던지기
            throw new IOException("EPUB 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}