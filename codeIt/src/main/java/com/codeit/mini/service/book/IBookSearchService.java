package com.codeit.mini.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codeit.mini.entity.book.BookEntity;

public interface IBookSearchService {
	Page<BookEntity> searchBook(String type, String keyword, Integer point, Pageable pageable);
}
