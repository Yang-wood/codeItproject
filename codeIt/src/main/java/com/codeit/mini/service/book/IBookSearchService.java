package com.codeit.mini.service.book;

import java.util.List;

import com.codeit.mini.entity.book.BookEntity;

public interface IBookSearchService {
	List<BookEntity> searchBook(String type, String keyword);
}
