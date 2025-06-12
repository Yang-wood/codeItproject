package com.codeit.mini.service.book.impl;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.repository.book.IBookRepository;
import com.codeit.mini.service.book.IBookService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BookServiceImpl implements IBookService {

    @Autowired
    private IBookRepository bookRepository;

    // 파일 저장 경로 설정
    private final String UPLOAD_BASE_DIR = System.getProperty("user.dir") + "/src/main/resources/static/upload/epubs";
    private final String EPUB_SUB_DIR = "files/";
    private final String COVER_IMAGE_SUB_DIR = "cover_images/";

    @Override
    @Transactional
    public void saveBook(BookDTO bookDTO, String tempEpubFilePath, String originalFileName) throws IOException {

        log.info("BookService.saveBook 메서드 진입");
        log.info("BookDTO: " + bookDTO.toString());
        log.info("임시 EPUB 파일 경로: " + tempEpubFilePath);
        log.info("원본 파일명: " + originalFileName);

        String finalEpubPath = null;
        Path sourceEpubPath = Paths.get(tempEpubFilePath);

        try {
            // 1. EPUB 파일 영구 저장
            // EPUB 저장 디렉토리 생성: UPLOAD_BASE_DIR + EPUB_SUB_DIR
            Path permanentEpubDir = Paths.get(UPLOAD_BASE_DIR, EPUB_SUB_DIR);
            Files.createDirectories(permanentEpubDir); // 디렉토리가 없으면 생성

            String uniqueEpubFileName = UUID.randomUUID().toString() + "_" + originalFileName; // 고유한 파일명 생성
            Path destinationEpubPath = permanentEpubDir.resolve(uniqueEpubFileName);

            // 임시 EPUB 파일을 영구 저장 위치로 이동
            Files.move(sourceEpubPath, destinationEpubPath, StandardCopyOption.REPLACE_EXISTING);
            finalEpubPath = destinationEpubPath.toString();
            log.info("EPUB 파일 영구 저장 완료: " + finalEpubPath);

        } catch (IOException e) {
            log.error("EPUB 파일 영구 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new IOException("EPUB 파일을 저장할 수 없습니다.", e); // 예외를 다시 던져서 상위에서 처리
        } finally {
            // 임시 EPUB 파일 삭제 시도
            try {
                Files.deleteIfExists(sourceEpubPath);
                log.info("임시 EPUB 파일 삭제 완료: " + tempEpubFilePath);
            } catch (IOException e) {
                log.warn("임시 EPUB 파일 삭제 실패: {}", e.getMessage());
            }
        }

        // 2. 표지 이미지 서버 파일 시스템에 저장 (Base64 데이터가 있을 경우)
        String coverImageWebPath = null; // DB에 저장될 웹 접근 가능한 상대 경로

        if (bookDTO.getCoverImageData() != null && bookDTO.getCoverImageData().length > 0) {
            try {
                // 표지 이미지 저장 디렉토리 생성: UPLOAD_BASE_DIR + COVER_IMAGE_SUB_DIR
                Path coverImageUploadDir = Paths.get(UPLOAD_BASE_DIR, COVER_IMAGE_SUB_DIR);
                Files.createDirectories(coverImageUploadDir); // 디렉토리가 없으면 생성

                // 이미지 확장자 유추 (BookDTO에 MIME 타입 정보가 있다면 더 정확하게 사용 가능)
                // 현재는 jpg로 고정, 필요시 bookDTO.getCoverImageDataBase64()에서 파싱하여 확장자 유추
                String imageExtension = "jpg"; // 기본값
                if (StringUtils.hasText(bookDTO.getCoverImageDataBase64()) &&
                    bookDTO.getCoverImageDataBase64().startsWith("data:image/")) {
                    String mimePart = bookDTO.getCoverImageDataBase64().split("data:image/")[1].split(";")[0];
                    if (mimePart.contains("png")) imageExtension = "png";
                    else if (mimePart.contains("gif")) imageExtension = "gif";
                }

                String coverImageFileName = UUID.randomUUID().toString() + "." + imageExtension; // 고유한 파일명 생성
                Path coverImagePath = coverImageUploadDir.resolve(coverImageFileName);

                Files.write(coverImagePath, bookDTO.getCoverImageData()); // byte[] 데이터를 파일로 저장

                // DB에 저장될 웹 접근 가능한 경로
                coverImageWebPath = "/upload/" + COVER_IMAGE_SUB_DIR + coverImageFileName;
                log.info("표지 이미지 서버 저장 완료: " + coverImagePath.toString() + ", 웹 경로: " + coverImageWebPath);

            } catch (IOException e) {
                log.error("표지 이미지 저장 중 오류 발생: {}", e.getMessage(), e);
                // 이미지 저장 실패 시, 경로를 null로 설정하여 DB에는 저장하지 않음 (선택 사항, throw도 가능)
                coverImageWebPath = null;
            }
        } else {
            log.warn("저장할 표지 이미지 데이터(byte[])가 없거나 비어있습니다. coverImageWebPath는 null.");
        }

        // 3. DTO의 데이터를 Book 엔터티로 변환 및 설정
        BookEntity bookEntity = new BookEntity();

        // 공백 제거 버전은 null 체크 후 처리
        bookEntity.setTitle(bookDTO.getTitle());
        bookEntity.setTitleNospace(bookDTO.getTitle() != null ? bookDTO.getTitle().replaceAll("\\s", "") : "");
        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setAuthorNospace(bookDTO.getAuthor() != null ? bookDTO.getAuthor().replaceAll("\\s", "") : "");
        bookEntity.setPublisher(bookDTO.getPublisher());
        bookEntity.setCategory(bookDTO.getCategory());
        bookEntity.setDescription(bookDTO.getDescription());

        // 최종적으로 저장된 파일의 경로를 엔터티에 설정
        bookEntity.setEpubPath(finalEpubPath); // 서버 파일 시스템의 절대 경로
        bookEntity.setCoverImg(coverImageWebPath); // 웹 접근 가능한 상대 경로 (DB 저장용)

        // 출판일 String을 LocalDateTime으로 파싱 시도
        String pubDateString = bookDTO.getPubDate();

        if (pubDateString != null && !pubDateString.equals("날짜 미상")) {
            try {
                // 'Z'가 있다면 제거 (LocalDateTime은 시간대 정보를 직접 처리하지 않으므로)
                String cleanedDateString = pubDateString.endsWith("Z") ? pubDateString.substring(0, pubDateString.length() - 1) : pubDateString;

                // 다양한 날짜 포맷 시도 (EPUBlib에서 오는 날짜 형식에 따라 유동적으로 처리)
                LocalDateTime parsedDateTime = null;
                try {
                    // 예: "yyyy-MM-dd'T'HH:mm:ss"
                    parsedDateTime = LocalDateTime.parse(cleanedDateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (DateTimeParseException e) {
                    try {
                        // 예: "yyyy-MM-dd"
                        parsedDateTime = LocalDate.parse(cleanedDateString, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
                    } catch (DateTimeParseException e2) {
                        // 예: "yyyy-MM" (월까지만 있는 경우)
                        parsedDateTime = LocalDate.parse(cleanedDateString + "-01", DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
                    }
                }

                bookEntity.setPubDate(parsedDateTime);
                log.info("[DEBUG] 출판일 파싱 성공: " + bookEntity.getPubDate());

            } catch (DateTimeParseException e) {
                log.warn("경고: 출판일 '{}' 파싱 실패. 오류: {}. pubDate를 null로 설정합니다.", pubDateString, e.getMessage());
                bookEntity.setPubDate(null);
            }
        } else {
            bookEntity.setPubDate(null);
        }

        // 추가적인 BookEntity 필드 초기화 (기본값 설정)
        bookEntity.setRentPoint(0);
        bookEntity.setRentCount(0);
        bookEntity.setWishCount(0);
        bookEntity.setAvgRating(0.0);
        bookEntity.setReviewCount(0);
        // regDate와 upDate는 @PrePersist/@PreUpdate 어노테이션을 사용하여 엔티티에서 자동 설정됩니다.

        // 4. Book 엔터티를 데이터베이스에 저장
        bookRepository.save(bookEntity); // save 메서드는 엔티티를 반환하지만, void로 처리 가능
        log.info("BookEntity DB 저장 완료: " + bookEntity.getBookId());
    }
}