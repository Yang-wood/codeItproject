package com.codeit.mini.controller.book;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.book.WishEntity;
import com.codeit.mini.repository.book.IBookRepository;
import com.codeit.mini.service.book.IBookSearchService;
import com.codeit.mini.service.book.IBookService;
import com.codeit.mini.service.book.IRentService;
import com.codeit.mini.service.book.IWishService;

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
	
	
	// 도서 대여
	@PostMapping("/rent")
	public ResponseEntity<RentEntity> rentBook(@RequestParam("bookId") Long bookId,
									  		   @RequestParam("memberId") Long memberId) {
		try {
			RentEntity rentEntity = rentService.rentBook(bookId, memberId);
			return new ResponseEntity<>(rentEntity, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	// 도서 찜추가
	@PostMapping("/addWish")
	public ResponseEntity<?> addWish(@RequestParam("bookId") Long bookId,
									 @RequestParam("memberId") Long memberId) {
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
	public ResponseEntity<String> removeWish(@RequestParam("bookId") Long bookId,
			  								 @RequestParam("memberId") Long memberId) {
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
	
	// 도서 필요 포인트 출력
//	@GetMapping()
//	public ResponseEntity<Map<String, Integer>> getBookCost(@PathVariable Long bookId) throws Exception {
//		
//		BookEntity bookEntity = bookRepository.findById(bookId)
//							.orElseThrow(()-> new IllegalAccessException("해당 도서가 없습니다."));
//		
//		Map<String, Integer> rs = new HashMap<>();
//		rs.put("pointCost", bookEntity.getRentPoint());
//		
//		return new ResponseEntity<>(rs, HttpStatus.OK);
//	}
	
}
