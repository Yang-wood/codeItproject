package com.codeit.mini.entity.epub;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.service.book.IBookService;

import jakarta.servlet.http.HttpSession; // 이 클래스는 현재 코드에서는 직접 사용되지 않지만, import가 필요할 수 있으니 남겨둡니다.
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.implementation.bind.annotation.Pipe;

@Controller
@Log4j2
@SessionAttributes("bookDTO")
public class EpubController {

    @Autowired
    private EpubService epubService;

    @Autowired
    private IBookService bookService;

    @GetMapping("/epubTest")
    public void epubGet() {
    	
    }

    
    @GetMapping("/uploadEpub")
    public String showUpload(Model model, SessionStatus sessionStatus) {
        if (model.containsAttribute("bookDTO")) {
            
        	sessionStatus.setComplete();
            log.info("uploadEpub GET: @SessionAttributes 'bookDTO' cleared.");
        }
        return "/uploadEpub";
    }

    
     // EPUB 파일을 업로드받아 정보를 추출한 후, 미리보기 화면으로 전달
    @PostMapping("/uploadEpub")
    public String uploadEpub(@RequestParam("epubFile") MultipartFile file, Model model,
                             RedirectAttributes rttr, HttpSession session) { // HttpSession은 현재 직접 사용되지 않음

        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "업로드된 파일이 비어있습니다.");
            return "/uploadEpub"; 
        }

        try {
            // 1. 파일을 서버의 임시 디렉토리에 저장
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            // 중복 방지를 위해 UUID와 원본 파일명을 조합하여 고유한 파일명 생성
            String uniqueFileName = UUID.randomUUID().toString() + "." + originalFileName;

            // 시스템 임시 디렉토리 (java.io.tmpdir) 아래에 'epub_temp' 폴더 생성
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "epub_temp");
            Files.createDirectories(tempDir); // 디렉토리가 없으면 생성

            Path tempFilePath = tempDir.resolve(uniqueFileName);
            file.transferTo(tempFilePath.toFile());

            log.info("EPUB 파일 임시 저장 완료: {}", tempFilePath.toString());

            // 2. BookDTO를 통해 EPUB 파일에서 메타데이터 추출 (임시 파일을 사용)
            BookDTO bookDTO = epubService.bookDTO(tempFilePath.toFile());
            model.addAttribute("bookDTO", bookDTO);

            // 3. 임시 파일 경로와 원본 파일명을 모델에 추가하여 "확인 후 저장" 폼으로 전달
            model.addAttribute("epubFilePath", tempFilePath.toString());
            model.addAttribute("originalFileName", originalFileName); // 원본 파일명도 함께 전달

            log.info("EPUB Info for preview: " + bookDTO);

        } catch (Exception e) {
            log.error("EPUB 파일 처리 중 오류가 발생했습니다: {}", e.getMessage(), e); // 에러 로그 상세화
            model.addAttribute("errorMessage", "EPUB 파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "/uploadEpub";
    }

    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute BookDTO bookDTO,
                           @RequestParam("epubFilePath") String epubFilePath,
                           @RequestParam("originalFileName") String originalFileName,
                           RedirectAttributes rttr, SessionStatus sessionStatus) {

        // 메서드 진입 시 로그
        log.info("saveBook 메서드 진입.");
        log.info("받은 BookDTO (DB 저장용): " + bookDTO.toString());
        log.info("저장할 임시 EPUB 파일 경로: " + epubFilePath);
        log.info("원본 파일명: " + originalFileName);

        // 임시 파일 경로가 유효한지 확인
        if (!StringUtils.hasText(epubFilePath) || !Files.exists(Paths.get(epubFilePath))) {
            rttr.addFlashAttribute("errorMessage", "저장할 EPUB 파일 정보를 찾을 수 없습니다. 다시 업로드해주세요.");
            return "redirect:/uploadEpub";
        }

        try {
            bookService.saveBook(bookDTO, epubFilePath, originalFileName);
            log.info("saveBook: BookService.saveBook 호출 완료.");

            sessionStatus.setComplete();
            log.info("SessionAttributes 'bookDTO' cleared.");

            rttr.addFlashAttribute("successMessage", "도서 정보가 성공적으로 저장되었습니다!");
            log.info("saveBook: 도서 정보 저장 성공");
            return "redirect:/uploadEpub";

        } catch (Exception e) {
            log.error("도서 정보 저장 중 서버 오류 발생: {}", e.getMessage(), e); 
            rttr.addFlashAttribute("errorMessage", "도서 정보 저장 중 서버 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/uploadEpub"; 
        }
    }
    
    @PostMapping("/multiUploadEpub")
    @ResponseBody
    public List<Map<String, String>> multiUploadEpub(@RequestParam("epubFiles") MultipartFile[] files) {
    	List<Map<String, String>> rs = new ArrayList<>();
    	log.info("multiUploadEpub.... :{}", files.length);
    	
    	Path temDir = Paths.get(System.getProperty("tmpdir"), "epub_temp");
    	try {
    		// 임시 디렉토리 생성
			Files.createDirectories(temDir);
		} catch (Exception e) {
			log.error("임시 디렉토리 생성 실패 : {}", e.getMessage(), e);
			Map<String, String> errRs = new HashMap<>();
			errRs.put("fileName", "NULL");
			errRs.put("status", "errer");
			errRs.put("message", "임시 디렉토리 생성 실패");
			rs.add(errRs);
			return rs;
		}
    	
    	for (MultipartFile file : files) {
			Map<String, String> result = new HashMap<>();
			String originFileName = StringUtils.cleanPath(file.getOriginalFilename());
			result.put("fileName", originFileName);
			
			if (file.isEmpty()) {
				result.put("message", "파일이 비어있습니다.");
				rs.add(result);
				continue;
			}
			
			if (!originFileName.toLowerCase().endsWith(".epub")) {
				result.put("message", "epub 파일만 가능합니다.");
				rs.add(result);
				continue;
			}
			
			String uniqueFileName = UUID.randomUUID().toString() + "_" + originFileName;
			Path tempFilePath = temDir.resolve(uniqueFileName);
			
			try {
				// 1. 파일 임시 저장
				file.transferTo(tempFilePath.toFile());
				log.info("파일 임시 저장 성공 : {}", tempFilePath.toString());
				
				// 2. 메타데이터 추출
				BookDTO bookDTO = epubService.bookDTO(tempFilePath.toFile());
				
				// 3. 임시 파일과 데이터로 저장
				bookService.saveBook(bookDTO, tempFilePath.toString(), originFileName);
				log.info("파일 저장 성공 : {}", bookDTO.getTitle());
				
			} catch (Exception e) {
				result.put("message", originFileName + " 파일 저장 중 에러");
			} finally {
				try {
					Files.deleteIfExists(tempFilePath);
					log.info("임시 파일 삭제 성공");
				} catch (Exception e2) {
					log.error("임시 파일 삭제 실패");
				}
			}
			rs.add(result);
		}
    	return rs;
    }
}