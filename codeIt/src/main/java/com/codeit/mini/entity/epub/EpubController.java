package com.codeit.mini.entity.epub;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.service.book.BookService;

import jakarta.servlet.http.HttpSession; // 이 클래스는 현재 코드에서는 직접 사용되지 않지만, import가 필요할 수 있으니 남겨둡니다.
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@SessionAttributes("bookDTO") // "bookDTO"라는 이름의 모델 속성을 세션에 저장하여 여러 요청 간에 유지
public class EpubController {

    @Autowired
    private EpubService epubService; // EPUB 파일에서 메타데이터를 추출하는 서비스

    @Autowired
    private BookService bookService; // 추출된 도서 정보를 최종적으로 저장하는 서비스

    @GetMapping("/epubTest")
    public void epubGet() {
        // 간단한 테스트용 GET 요청 매핑. 특별한 로직 없이 뷰 이름과 동일한 템플릿 반환
    }

    /*
     * EPUB 업로드 페이지를 보여줍니다.
     * GET 요청 시, 이전에 세션에 저장된 bookDTO가 있다면 초기화하거나 제거합니다.
     *
     * @param model 모델 객체
     * @param sessionStatus 세션 상태를 관리하는 객체 (세션 속성 완료 처리용)
     * @return 업로드 페이지 뷰 이름
     */
    @GetMapping("/uploadEpub")
    public String showUpload(Model model, SessionStatus sessionStatus) {
        // @SessionAttributes에 의해 세션에 bookDTO가 남아있을 수 있으므로,
        // 새 업로드를 시작할 때는 세션의 bookDTO를 초기화하거나 제거하는 것이 일반적입니다.
        if (model.containsAttribute("bookDTO")) {
            // 새 BookDTO 객체를 모델에 넣어 폼을 초기화 (선택 사항)
            // model.addAttribute("bookDTO", new BookDTO());

            // 또는 @SessionAttributes로 지정된 모든 세션 속성을 제거하여 완전히 초기화
            sessionStatus.setComplete();
            log.info("uploadEpub GET: @SessionAttributes 'bookDTO' cleared.");
        }
        return "/uploadEpub"; // uploadEpub.html 또는 uploadEpub.jsp 등의 뷰를 찾아 반환
    }

    /*
     * EPUB 파일을 업로드받아 정보를 추출한 후, 미리보기 화면으로 전달합니다.
     * 업로드된 파일은 서버의 임시 디렉토리에 저장됩니다.
     *
     * @param file 업로드된 EPUB 파일
     * @param model 모델 객체
     * @param rttr 리다이렉트 시 플래시 속성 전달용
     * @return 미리보기 또는 업로드 페이지 뷰 이름
     */
    @PostMapping("/uploadEpub")
    public String uploadEpub(@RequestParam("epubFile") MultipartFile file, Model model,
                             RedirectAttributes rttr, HttpSession session) { // HttpSession은 현재 직접 사용되지 않음

        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "업로드된 파일이 비어있습니다.");
            return "/uploadEpub"; // 파일이 비어있으면 다시 업로드 페이지로 이동
        }

        try {
            // 1. 파일을 서버의 임시 디렉토리에 저장
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            // 중복 방지를 위해 UUID와 원본 파일명을 조합하여 고유한 파일명 생성
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

            // 시스템 임시 디렉토리 (java.io.tmpdir) 아래에 'epub_temp' 폴더 생성
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "epub_temp");
            Files.createDirectories(tempDir); // 디렉토리가 없으면 생성

            Path tempFilePath = tempDir.resolve(uniqueFileName);
            // 업로드된 파일을 임시 파일로 저장
            file.transferTo(tempFilePath.toFile());

            log.info("EPUB 파일 임시 저장 완료: {}", tempFilePath.toString());

            // 2. BookDTO를 통해 EPUB 파일에서 메타데이터 추출 (임시 파일을 사용)
            // MultipartFile 대신 File 객체(tempFilePath.toFile())를 전달하여 메타데이터 추출 서비스 호출
            BookDTO bookDTO = epubService.bookDTO(tempFilePath.toFile());
            model.addAttribute("bookDTO", bookDTO); // 추출된 DTO를 Model에 담아 뷰로 전달 (-> @SessionAttributes로 세션에도 저장)

            // 3. 임시 파일 경로와 원본 파일명을 모델에 추가하여 "확인 후 저장" 폼으로 전달
            // 이 정보는 다음 POST 요청인 /saveBook 에서 사용됩니다.
            model.addAttribute("epubFilePath", tempFilePath.toString());
            model.addAttribute("originalFileName", originalFileName); // 원본 파일명도 함께 전달

            log.info("EPUB Info for preview: " + bookDTO);

        } catch (Exception e) {
            log.error("EPUB 파일 처리 중 오류가 발생했습니다: {}", e.getMessage(), e); // 에러 로그 상세화
            model.addAttribute("errorMessage", "EPUB 파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "/uploadEpub"; // 미리보기 정보를 포함하여 다시 업로드 페이지 뷰로 이동 (폼 액션이 동일한 경우)
    }

    /*
     * 미리보기 정보를 확인한 후, 도서 정보를 최종적으로 파일 시스템 및 DB에 저장합니다.
     * @ModelAttribute로 세션의 bookDTO를 가져오고, @RequestParam으로 임시 파일 경로를 받습니다.
     *
     * @param bookDTO 세션에 저장된 BookDTO (수정되었을 수도 있음)
     * @param epubFilePath 임시 EPUB 파일의 경로
     * @param originalFileName 원본 EPUB 파일명
     * @param rttr 리다이렉트 시 플래시 속성 전달용
     * @param sessionStatus 세션 상태를 관리하는 객체 (세션 속성 완료 처리용)
     * @return 리다이렉트 URL
     */
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
            return "redirect:/uploadEpub"; // 파일 경로가 유효하지 않으면 업로드 페이지로 리다이렉트
        }

        try {
            // BookService를 통해 비즈니스 로직(파일 영구 저장, DB 저장) 수행
            // DTO와 임시 파일 경로, 원본 파일명을 서비스로 전달
            bookService.saveBook(bookDTO, epubFilePath, originalFileName);
            log.info("saveBook: BookService.saveBook 호출 완료.");

            // @SessionAttributes로 지정된 'bookDTO' 세션 속성을 제거
            sessionStatus.setComplete();
            log.info("SessionAttributes 'bookDTO' cleared.");

            // 성공 메시지 설정 후 업로드 페이지로 리다이렉트
            rttr.addFlashAttribute("successMessage", "도서 정보가 성공적으로 저장되었습니다!");
            log.info("saveBook: 도서 정보 저장 성공. 메시지 설정 후 리다이렉션.");
            return "redirect:/uploadEpub";

        } catch (Exception e) {
            log.error("도서 정보 저장 중 서버 오류 발생: {}", e.getMessage(), e); // 에러 로그 상세화
            rttr.addFlashAttribute("errorMessage", "도서 정보 저장 중 서버 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/uploadEpub"; // 오류 발생 시에도 업로드 페이지로 리다이렉트
        }
    }
}