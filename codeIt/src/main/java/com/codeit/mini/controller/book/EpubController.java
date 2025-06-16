package com.codeit.mini.controller.book;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.service.book.EpubService;
import com.codeit.mini.service.book.IBookService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@SessionAttributes("bookDTO")
@RequestMapping("/admin")
public class EpubController {

    @Autowired
    private EpubService epubService;

    @Autowired
    private IBookService bookService;

    @GetMapping("/uploadEpub")
    public String showUpload(Model model, SessionStatus sessionStatus, HttpSession session) {
        if (model.containsAttribute("bookDTO")) {
            sessionStatus.setComplete();
            log.info("@SessionAttributes 'bookDTO' 초기화 완료.");
        }
        return "book/epub/uploadEpub";
    }

    @PostMapping("/uploadEpub")
    public String uploadEpub(@RequestParam("epubFile") MultipartFile file, Model model,
                             RedirectAttributes rttr, HttpSession session) {

        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "업로드된 파일이 비어있습니다.");
            log.warn("업로드된 파일이 비어 있음.");
            return "book/epub/uploadEpub";
        }

        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID() + "." + originalFileName;

            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "epub_temp");
            Files.createDirectories(tempDir);

            Path tempFilePath = tempDir.resolve(uniqueFileName);
            file.transferTo(tempFilePath.toFile());

            log.info("EPUB 파일 임시 저장 완료: {}", tempFilePath);

            BookDTO bookDTO = epubService.bookDTO(tempFilePath.toFile());
            model.addAttribute("bookDTO", bookDTO);
            model.addAttribute("epubFilePath", tempFilePath.toString());
            model.addAttribute("originalFileName", originalFileName);

            log.info("EPUB 메타데이터 추출 완료: {}", bookDTO);

        } catch (Exception e) {
            log.error("EPUB 파일 처리 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "EPUB 파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "book/epub/uploadEpub";
    }

    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute BookDTO bookDTO,
                           @RequestParam("epubFilePath") String epubFilePath,
                           @RequestParam("originalFileName") String originalFileName,
                           @RequestParam("rentPoint") int rentPoint,
                           RedirectAttributes rttr, SessionStatus sessionStatus) {

        log.info("POST /saveBook 호출됨");
        log.info("저장 대상 BookDTO: {}", bookDTO);
        log.info("EPUB 임시 파일 경로: {}", epubFilePath);
        log.info("원본 파일명: {}", originalFileName);

        if (!StringUtils.hasText(epubFilePath) || !Files.exists(Paths.get(epubFilePath))) {
            rttr.addFlashAttribute("errorMessage", "저장할 EPUB 파일 정보를 찾을 수 없습니다. 다시 업로드해주세요.");
            log.warn("임시 EPUB 파일이 존재하지 않음.");
            return "redirect:/admin/uploadEpub";
        }

        try {
            bookService.saveBook(bookDTO, epubFilePath, originalFileName, rentPoint);
            log.info("도서 정보 저장 완료.");

            sessionStatus.setComplete();
            log.info("@SessionAttributes 'bookDTO' 초기화 완료.");

            rttr.addFlashAttribute("successMessage", "도서 정보가 성공적으로 저장되었습니다!");
            return "redirect:/admin/uploadEpub";

        } catch (Exception e) {
            log.error("도서 정보 저장 중 오류 발생: {}", e.getMessage(), e);
            rttr.addFlashAttribute("errorMessage", "도서 정보 저장 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/admin/uploadEpub";
        }
    }

    @PostMapping("/multiUploadEpub")
    @ResponseBody
    public List<Map<String, String>> multiUploadEpub(
    						@RequestParam("epubFiles") MultipartFile[] files,
    						@RequestParam("rentPoint") int rentPoint) {
        List<Map<String, String>> results = new ArrayList<>();
        log.info("POST /multiUploadEpub 호출됨 - 총 {}개 파일", files.length);

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "epub_temp");

        try {
            Files.createDirectories(tempDir);
        } catch (Exception e) {
            log.error("임시 디렉토리 생성 실패: {}", e.getMessage(), e);
            Map<String, String> errResult = new HashMap<>();
            errResult.put("fileName", "NULL");
            errResult.put("status", "error");
            errResult.put("message", "임시 디렉토리 생성 실패");
            results.add(errResult);
            return results;
        }

        for (MultipartFile file : files) {
            Map<String, String> result = new HashMap<>();
            String originFileName = StringUtils.cleanPath(file.getOriginalFilename());
            result.put("fileName", originFileName);

            if (file.isEmpty()) {
            	result.put("status", "error");
                result.put("message", "파일이 비어있습니다.");
                log.warn("{} 파일이 비어 있음", originFileName);
                results.add(result);
                continue;
            }

            if (!originFileName.toLowerCase().endsWith(".epub")) {
            	result.put("status", "error");
                result.put("message", "epub 파일만 가능합니다.");
                log.warn("업로드 실패 - {}: EPUB 파일이 아님", originFileName);
                results.add(result);
                continue;
            }

            String uniqueFileName = UUID.randomUUID() + "_" + originFileName;
            Path tempFilePath = tempDir.resolve(uniqueFileName);

            try {
                file.transferTo(tempFilePath.toFile());
                log.info("임시 저장 완료: {}", tempFilePath);

                BookDTO bookDTO = epubService.bookDTO(tempFilePath.toFile());
                bookService.saveBook(bookDTO, tempFilePath.toString(), originFileName, rentPoint);
                log.info("도서 저장 완료: {}", bookDTO.getTitle());
                result.put("status", "success");
                result.put("message", "성공");

            } catch (Exception e) {
            	result.put("status", "error");
                result.put("message", originFileName + " 파일 저장 중 오류");
                log.error("{} 처리 중 오류 발생: {}", originFileName, e.getMessage(), e);
            } finally {
                try {
                    Files.deleteIfExists(tempFilePath);
                    log.info("임시 파일 삭제 완료: {}", tempFilePath);
                } catch (Exception e2) {
                    log.warn("임시 파일 삭제 실패: {}", tempFilePath, e2);
                }
            }

            results.add(result);
        }

        return results;
    }
}
