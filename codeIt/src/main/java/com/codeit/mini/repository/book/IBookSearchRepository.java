package com.codeit.mini.repository.book;

import java.util.List;

import com.codeit.mini.entity.book.BookEntity;

public interface IBookSearchRepository {
	List<BookEntity> searchBook(String type, String keyword);
	
}
