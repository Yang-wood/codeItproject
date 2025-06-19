package com.codeit.mini.controller.book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.service.book.IBookSearchService;
import com.codeit.mini.service.book.IBookService;
import com.codeit.mini.service.book.IRentService;
import com.codeit.mini.service.book.IReviewService;
import com.codeit.mini.service.book.IWishService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
@Log4j2
public class MyBookpageController {
	
	private final IBookService bookService;
	private final IRentService rentService;
	private final IWishService wishService;
	private final IReviewService reviewService;
	
	// 내 서재- 위시리스트 이동
	@GetMapping("/wishList")
	public String wishList(@RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "10") int size,
						   Model model, HttpSession session) {
		
		MemberDTO member = (MemberDTO) session.getAttribute("member");
		
		if (member == null) {
			return "member/login";
		}
		
		Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
		Pageable pageable = PageRequest.of(page, size, sort);
	    Page<BookDTO> dtoPage = Page.empty(pageable);
	    Long finalMemberId = null;
	    
	    try {
	        if (member != null) {
	            finalMemberId = member.getMemberId();
	        }

	        log.info("wishList: session member = " + (member != null ? member.getMemberName() : "null"));
	        log.info("wishList: finalMemberId = " + finalMemberId);

	        if (finalMemberId == null) {
	            model.addAttribute("wishList", List.of());
	            model.addAttribute("page", Page.empty(pageable));
	            return "member/login";
	        }

	        Page<BookEntity> wishListPage = wishService.findWishListByMemberId(finalMemberId, pageable);
	        
	        int totalPages = wishListPage.getTotalPages();
            int currentPage = wishListPage.getNumber();
            int PageNum = 10;
            int startPage = (currentPage / PageNum) * PageNum;
            int endPage = Math.min(startPage + PageNum - 1, totalPages - 1);
            
            if (totalPages == 0) { 
                startPage = 0;
                endPage = 0;
            } else if (endPage - startPage + 1 < PageNum && totalPages >= PageNum) {
                
            	startPage = Math.max(0, endPage - PageNum + 1);
            }
	        
	        final Long finalMemberIdRamda = finalMemberId;
	        dtoPage = wishListPage.map(bookEntity -> {
	            BookDTO bookDTO = bookService.entityToDto(bookEntity);
	            try {
	                bookDTO.setRentedByCurrentUser(rentService.isRented(bookEntity.getBookId(), finalMemberIdRamda));
	            } catch (Exception e) {
	                bookDTO.setRentedByCurrentUser(false);
	            }
	            try {
	                bookDTO.setWishedByCurrentUser(true); // 위시리스트 책이므로 무조건 true
	            } catch (Exception e) {
	                bookDTO.setWishedByCurrentUser(false);
	            }
	            
	            double avg = bookDTO.getAvgRating(); // avgRating은 0.0 ~ 5.0 범위의 실수
	            int rating10 = (int) Math.round(avg * 10); // 예: 4.3 → 43 → 반올림 43
	            int full = rating10 / 10; // 정수부
	            boolean half = (rating10 % 10) >= 5;
	            int empty = 5 - full - (half ? 1 : 0);

	            bookDTO.setFullStar(full);
	            bookDTO.setHalfStar(half);
	            bookDTO.setEmptyStar(empty);
	            
	            log.info("Book Title: " + bookDTO.getTitle() + 
	                    ", AvgRating: " + bookDTO.getAvgRating() + 
	                    ", FullStar: " + bookDTO.getFullStar() + 
	                    ", HalfStar: " + bookDTO.isHalfStar() + 
	                    ", EmptyStar: " + bookDTO.getEmptyStar());
	            
	            return bookDTO;
	        });

	        model.addAttribute("wishList", dtoPage.getContent());
	        model.addAttribute("page", dtoPage);
	        model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

	    } catch (Exception e) {
	        log.error("도서 위시리스트 페이지 로드 중 오류 발생: {}", e.getMessage(), e);
	        model.addAttribute("errorMessage", "로드 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
	        model.addAttribute("wishList", List.of());
	        model.addAttribute("page", Page.empty(pageable));
	    }

	    model.addAttribute("finalmemberId", finalMemberId);
	    log.info("finalMemberId in model = {}", model.getAttribute("finalMemberId"));

	    return "book/wishList";
	}
	
	// 내 서재 - 대여목록 이동
	@GetMapping("/rentList")
	public String rentList(@RequestParam(value = "page", defaultValue = "0") int page,
			   			   @RequestParam(value = "size", defaultValue = "10") int size,
			   			   Model model, HttpSession session) {
		
		MemberDTO member = (MemberDTO) session.getAttribute("member");
		
		if (member == null) {
			return "member/login";
		}
		
		Sort sort = Sort.by(Sort.Direction.DESC, "rentDate");
		Pageable pageable = PageRequest.of(page, size, sort);
	    Page<BookDTO> dtoPage = Page.empty(pageable);
	    Long finalMemberId = null;

	    try {
	        if (member != null) {
	            finalMemberId = member.getMemberId();
	        }

	        log.info("rentList: session member = " + (member != null ? member.getMemberName() : "null"));
	        log.info("rentList: finalMemberId = " + finalMemberId);

	        if (finalMemberId == null) {
	            model.addAttribute("rentList", List.of());
	            model.addAttribute("page", Page.empty(pageable));
	            return "member/login";
	        }

	        Page<RentEntity> rentListPage = rentService.findRentListByMemberId(finalMemberId, pageable);
	       
	        int totalPages = rentListPage.getTotalPages();
            int currentPage = rentListPage.getNumber();
            int PageNum = 10;
            int startPage = (currentPage / PageNum) * PageNum;
            int endPage = Math.min(startPage + PageNum - 1, totalPages - 1);
            
            if (totalPages == 0) { 
                startPage = 0;
                endPage = 0;
            } else if (endPage - startPage + 1 < PageNum && totalPages >= PageNum) {
                
            	startPage = Math.max(0, endPage - PageNum + 1);
            }
	        
	        final Long finalMemberIdRamda = finalMemberId;
	        dtoPage = rentListPage.map(rentEntity -> {
	            BookDTO bookDTO = bookService.entityToDto(rentEntity.getBookEntity());
	            bookDTO.setRentId(rentEntity.getRentId());
	            try {
	                bookDTO.setWishedByCurrentUser(wishService.isWished(bookDTO.getBookId(), finalMemberIdRamda));
	            } catch (Exception e) {
	                bookDTO.setWishedByCurrentUser(false);
	            }
	            try {
	                bookDTO.setRentedByCurrentUser(true); // 대여는 기본값!
	            } catch (Exception e) {
	                bookDTO.setRentedByCurrentUser(false);
	            }
	            
	            Long currentRentId = rentEntity.getRentId();
	            log.info("Processing RentEntity with rentId: {}", currentRentId);
	            
	            try {
					boolean isReview = reviewService.hasReview(currentRentId);
					log.info("For rentId: {}, reviewService.hasReview returned: {}", currentRentId, isReview);
					bookDTO.setReviewByCurrentUser(isReview);
				} catch (Exception e) {
					log.error("Error checking review for rentId {}: {}", currentRentId, e.getMessage(), e); // <--- 로그 메시지 명확화
					bookDTO.setReviewByCurrentUser(false);
				}
	            
	            double avg = bookDTO.getAvgRating(); // avgRating은 0.0 ~ 5.0 범위의 실수
	            int rating10 = (int) Math.round(avg * 10); // 예: 4.3 → 43 → 반올림 43
	            int full = rating10 / 10; // 정수부
	            boolean half = (rating10 % 10) >= 5;
	            int empty = 5 - full - (half ? 1 : 0);

	            bookDTO.setFullStar(full);
	            bookDTO.setHalfStar(half);
	            bookDTO.setEmptyStar(empty);
	            
	            log.info("Book Title: " + bookDTO.getTitle() + 
	                    ", AvgRating: " + bookDTO.getAvgRating() + 
	                    ", FullStar: " + bookDTO.getFullStar() + 
	                    ", HalfStar: " + bookDTO.isHalfStar() + 
	                    ", EmptyStar: " + bookDTO.getEmptyStar());
	            
	            log.info("BookDTO for rentId {}: reviewByCurrentUser = {}", currentRentId, bookDTO.isReviewByCurrentUser());
	            return bookDTO;
	        });

	        model.addAttribute("rentList", dtoPage.getContent());
	        model.addAttribute("page", dtoPage);
	        model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

	    } catch (Exception e) {
	        log.error("도서 대여리스트 페이지 로드 중 오류 발생: {}", e.getMessage(), e);
	        model.addAttribute("errorMessage", "로드 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
	        model.addAttribute("rentList", List.of());
	        model.addAttribute("page", Page.empty(pageable));
	    }

	    model.addAttribute("finalmemberId", finalMemberId);
	    log.info("finalMemberId in model = {}", model.getAttribute("finalMemberId"));
	    

	    return "book/rentList";
	}
}
