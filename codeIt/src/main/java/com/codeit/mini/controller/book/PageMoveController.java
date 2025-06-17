package com.codeit.mini.controller.book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.service.book.IBookSearchService;
import com.codeit.mini.service.book.IBookService;
import com.codeit.mini.service.book.IRentService;
import com.codeit.mini.service.book.IWishService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
@Log4j2
public class PageMoveController {
	
	private final IBookService bookService;
	private final IBookSearchService searchService;
	private final IRentService rentService;
	private final IWishService wishService;
	
	// 검색 결과 이동
	@GetMapping("/search")
	public String searchPage(@RequestParam(value = "keyword", required = false) String keyword,
							 @RequestParam(value = "type", defaultValue = "all") String type,
							 @RequestParam(value = "point", required = false) Integer point,
							 @RequestParam(value = "page", defaultValue = "0") int page,
							 @RequestParam(value = "size", defaultValue = "20") int size,
							 Model model, HttpSession session) {
		
		Pageable pageable = PageRequest.of(page, size);
		Page<BookDTO> dtoPage = Page.empty(pageable);
		
		Long finalMemberId = null;
		
		try {
			
			MemberDTO member = (MemberDTO)session.getAttribute("member");
			
			if (member != null) {
				finalMemberId = member.getMemberId();
			}
			
			log.info("searchPage: session member = " + (member != null ? member.getMemberName() : "null"));
            log.info("searchPage: finalmemberId = " + finalMemberId);
			
            Page<BookEntity> searchList = searchService.searchBook(type, keyword, point, pageable);
	        
            final Long finalMemberIdLambda = finalMemberId;
            
            dtoPage = searchList.map(bookEntity -> {
	        	BookDTO bookDTO = bookService.entityToDto(bookEntity);
	        	try {
	        		if (finalMemberIdLambda != null) {
	        			bookDTO.setRentedByCurrentUser(rentService.isRented(bookEntity.getBookId(), finalMemberIdLambda));
	        		} else {
	        			bookDTO.setRentedByCurrentUser(false);
	        		}
				} catch (Exception e) {
                    bookDTO.setRentedByCurrentUser(false);
				}
	        	try {
	        		if (finalMemberIdLambda != null) {
	        			bookDTO.setWishedByCurrentUser(wishService.isWished(bookEntity.getBookId(), finalMemberIdLambda));
	        		} else {
	        			bookDTO.setWishedByCurrentUser(false);
	        		}
				} catch (Exception e) {
                    bookDTO.setWishedByCurrentUser(false);
				}
	        	return bookDTO;
	        });
			model.addAttribute("books", dtoPage.getContent());
			model.addAttribute("page", dtoPage);
			
		} catch (Exception e) {
			log.error("도서 검색 페이지 로드 중 심각한 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "도서 검색 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            model.addAttribute("books", List.of());
            model.addAttribute("page", Page.empty(pageable));
		}
		
		model.addAttribute("keyword", keyword);
        model.addAttribute("type", type);
        model.addAttribute("point", point);
        model.addAttribute("finalmemberId", finalMemberId);
        log.info(model.getAttribute("finalmemberId"));
		return "book/searchResult";
	}
	
	// 도서 상세 이동
	@GetMapping("/{bookId}")
	public String readPage(@PathVariable Long bookId, Model model) {
		try {
			
			BookDTO bookDTO = bookService.getBookById(bookId);
			
			if (bookDTO != null) {
				model.addAttribute("bookDTO", bookDTO);
			} else {
				return "error/404";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "book/read";
	}
	
	// 도서 열람 - epubReader 연결
	@GetMapping("/epubRead")
	public String epubRead(@RequestParam("bookId") Long bookId, Model model,
							HttpSession session) {
		
		MemberDTO member = (MemberDTO) session.getAttribute("member");
		
		if (member == null) {
			return "member/login";
		}
		
		try {
			BookEntity bookEntity = bookService.findByBookId(bookId);
			
			if (bookEntity != null && bookEntity.getEpubPath() != null) {
				String epubPath = bookEntity.getEpubPath();
				model.addAttribute("epubPath", epubPath);
				log.info("연결할 경로 : {}", epubPath);
			} else {
				log.warn("NOT epub");
				model.addAttribute("errorMessage", "NOT FOUND EPUB");
			}
		} catch (Exception e) {
			log.error("SERVER ERROR");
			model.addAttribute("errorMessage", "SERVER ERROR");
		}
		
		return "book/epub/epubReader";
	}
	
	// 서브 도서 목록 이동
	@GetMapping("/subList")
	public String subListPage() {
		return "book/subList";
	}
	
	
	// 관리자 페이지이동
	@GetMapping("/admin")
	public String adminPage() {
		return "book/admin";
	}
	
}
