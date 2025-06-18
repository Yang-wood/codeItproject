package com.codeit.mini.controller.book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.dto.book.ReviewDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.book.ReviewEntity;
import com.codeit.mini.entity.book.WishEntity;
import com.codeit.mini.repository.book.IBookRepository;
import com.codeit.mini.service.book.IBookSearchService;
import com.codeit.mini.service.book.IBookService;
import com.codeit.mini.service.book.IRentService;
import com.codeit.mini.service.book.IReviewService;
import com.codeit.mini.service.book.IWishService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Log4j2
public class RestAPIController {
	
	private final IBookRepository bookRepository;
	private final IBookSearchService searchService;
	private final IBookService bookService;
	private final IRentService rentService;
	private final IWishService wishService;
	private final IReviewService reviewService;
	
	// 도서 검색
	@GetMapping("/search")
	public ResponseEntity<Page<BookDTO>> searchBook(@RequestParam(value = "keyword", required = false) String keyword,
			 										@RequestParam(value = "type", defaultValue = "all") String type,
			 										@RequestParam(value = "point", required = false) Integer point,
			 										@PageableDefault(size = 20) Pageable pageable) {
		
		Page<BookEntity> searchList = searchService.searchBook(type, keyword, point, pageable);
		
		Page<BookDTO> dtoPage = searchList.map(bookService::entityToDto);
		
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}
	
	// 회원 포인트 출력
	@GetMapping("/getPoint")
	public ResponseEntity<?> getMemberPoint(HttpSession session) throws Exception {
		MemberDTO member = (MemberDTO)session.getAttribute("member");
		
		if (member == null) {
			return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		int point = member.getPoints();
		
		if (point < 0 ) {
			return new ResponseEntity<>("잔여 포인트에 오류", HttpStatus.BAD_REQUEST);
		} 
		Map<String, Object> result = new HashMap<>();
		result.put("memberId", member.getMemberId());
		result.put("name", member.getMemberName());
		result.put("points", point);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	// 도서 대여
	@PostMapping("/rent")
	public ResponseEntity<?> rentBook(@RequestParam("bookId") Long bookId, HttpSession session) {
		MemberDTO member = (MemberDTO)session.getAttribute("member");
		
		if (member == null) {
			return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		Long memberId = member.getMemberId();
		
		try {
			RentEntity rentEntity = rentService.rentBook(bookId, memberId);
			return new ResponseEntity<>(rentEntity, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 도서 찜추가
	@PostMapping("/addWish")
	public ResponseEntity<?> addWish(@RequestParam("bookId") Long bookId, HttpSession session) {
		MemberDTO member = (MemberDTO) session.getAttribute("member");
		
		if (member == null) {
			return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		Long memberId = member.getMemberId();
		try {
			WishEntity wishEntity = wishService.addWished(bookId, memberId);
			return new ResponseEntity<>(wishEntity, HttpStatus.OK);
		} catch (IllegalAccessException e) {
			if (e.getMessage().contains("NOT Member") || e.getMessage().contains("NOT Book")) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
	        } else if (e.getMessage().equals("중복")) {
	            return new ResponseEntity<>("이미 찜한 도서입니다.", HttpStatus.CONFLICT); // 409 Conflict
	        }
	        return new ResponseEntity<>("처리 중 오류: " + e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>("내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
		}
	}
	
	// 도서 찜 삭제
	@DeleteMapping("/removeWish")
	public ResponseEntity<?> removeWish(@RequestParam("bookId") Long bookId, HttpSession session) {
		MemberDTO member = (MemberDTO)session.getAttribute("member");
		
		if (member == null) {
			return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		Long memberId =  member.getMemberId();
		
		try {
			boolean removed = wishService.removeWished(bookId, memberId);
			if (removed) {
				return new ResponseEntity<>("성공", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("에러", HttpStatus.NOT_FOUND);
			}
		} catch (IllegalAccessException  e) {
			if (e.getMessage().contains("NOT Member") || e.getMessage().contains("NOT Book")) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); 
	        }
	        return new ResponseEntity<>("찜 삭제 처리 중 오류: " + e.getMessage(), HttpStatus.BAD_REQUEST); 
		} catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 리뷰 등록
	@PostMapping("/regReview")
	public ResponseEntity<?> regReview(@RequestBody ReviewDTO reviewDTO, HttpSession session) {
		
		MemberDTO member = (MemberDTO) session.getAttribute("member");
		
		if (member == null) {
			return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		try {
			reviewDTO.setMemberId(member.getMemberId());
			ReviewEntity reviewEntity = reviewService.regReview(reviewDTO);
			return new ResponseEntity<>(reviewEntity, HttpStatus.OK);
			
		} catch (IllegalStateException e) {
			return new ResponseEntity<>("리뷰가 있습니다.", HttpStatus.CONFLICT);
		} catch (IllegalAccessException e) {
			return new ResponseEntity<>("NOT RENT", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 리뷰 삭제
	@DeleteMapping("/removeReview/{reviewId}")
	public ResponseEntity<?> removeReview(@PathVariable("reviewId") Long reviewId, HttpSession session) {
		MemberDTO member = (MemberDTO) session.getAttribute("member");
		if (member == null) {
			return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		try {
			Long memberId = member.getMemberId();
			
			ReviewDTO reviewDTO = reviewService.findById(reviewId);
			
			if (reviewDTO == null) {
				return new ResponseEntity<>("NOT REVIEW", HttpStatus.NOT_FOUND);
			}
			
			if (!reviewDTO.getMemberId().equals(memberId)) {
				return new ResponseEntity<>("NOT MEMBER", HttpStatus.FORBIDDEN);
			}
			
			reviewService.removeReview(reviewId);
			
			return new ResponseEntity<>("REMOVE REVIEW", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	// 리뷰 수정
	@PutMapping("/modifyReview/{reviewId}")
	public ResponseEntity<?> modifyReview(@PathVariable("reviewId") Long reviewId,
										  @RequestBody ReviewDTO reviewDTO,
										  HttpSession session) {
		try {
			MemberDTO member = (MemberDTO) session.getAttribute("member");
			if (member == null) {
				return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
			}
			
			reviewDTO.setReviewId(reviewId);
			reviewDTO.setMemberId(member.getMemberId());
			
			reviewService.modifyReview(reviewDTO);
			return new ResponseEntity<>("MODIFY REVIEW", HttpStatus.OK);
		} catch (Exception e) {
			log.error("SERVER ERROR", e.getMessage());
			return new ResponseEntity<>("SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 회원 리뷰 리스트 출력
	@GetMapping("/myReviewList/{bookId}")
	public ResponseEntity<?> getMyReviewList(@PathVariable("bookId") Long bookId, HttpSession session) {
		MemberDTO member =(MemberDTO) session.getAttribute("member");
		
		if (member == null) {
			return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
		}
		try {
			Long memberId = member.getMemberId();
			List<ReviewDTO> review = reviewService.findReviewByMemberId(bookId, memberId);
			return new ResponseEntity<>(review, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("리뷰 조회 중 에러", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
