package com.codeit.mini.service.book;

import com.codeit.mini.entity.book.RentEntity;

public interface IRentService {
	
	RentEntity rentBook(Long bookId, Long memberId) throws Exception;
	
	void rentEndList() throws Exception;
	
	boolean isRented(Long bookId, Long memberId) throws Exception;

}
