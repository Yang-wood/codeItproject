package com.codeit.mini.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;

public interface IRentService {
	
	RentEntity rentBook(Long bookId, Long memberId) throws Exception;
	
	void rentEndList() throws Exception;
	
	boolean isRented(Long bookId, Long memberId) throws Exception;
	
	Page<RentEntity> findRentListByMemberId(Long memberId, Pageable pageable) throws Exception;
	
}
