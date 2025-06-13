package com.codeit.mini.controller.book;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.service.book.IBookSearchService;
import com.codeit.mini.service.book.IBookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class RestAPIController {
	
	private final IBookSearchService searchService;
	
	private final IBookService bookService;
	
	@GetMapping("/search")
	public ResponseEntity<List<BookDTO>> searchBook(@RequestParam String keyword,
            										@RequestParam String type) {
		
		List<BookDTO> searchList = searchService.searchBook(type, keyword).stream()
								.map(bookService::entityToDto)
								.collect(Collectors.toList());
		
		return new ResponseEntity<>(searchList, HttpStatus.OK);
	}
	
	
}
