package com.codeit.mini.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.WishEntity;

public interface IWishService {
	
	WishEntity addWished(Long bookId, Long memberId) throws Exception;
	
	boolean removeWished(Long bookId, Long memberId) throws Exception;
	
	boolean isWished(Long memberId, Long bookId) throws Exception;
	
	Page<BookEntity> findWishListByMemberId(Long memberId, Pageable pageable) throws Exception;
	
}
