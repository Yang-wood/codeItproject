package com.codeit.mini.service.book.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.repository.book.IBookSearchRepository;
import com.codeit.mini.service.book.IBookSearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements IBookSearchService{
	
	private final IBookSearchRepository bookSearchRepository;
	
	@Override
	public Page<BookEntity> searchBook(String type, String keyword, Integer point, Pageable pageable) {
		return bookSearchRepository.searchBook(type, keyword, point, pageable);
	}

}
