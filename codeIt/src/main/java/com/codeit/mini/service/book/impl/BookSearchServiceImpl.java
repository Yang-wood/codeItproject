package com.codeit.mini.service.book.impl;

import java.util.List;

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
	public List<BookEntity> searchBook(String type, String keyword) {
		return bookSearchRepository.searchBook(type, keyword);
	}

}
