package com.codeit.mini.service.book.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.WishEntity;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.repository.book.IBookRepository;
import com.codeit.mini.repository.book.IWishRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.service.book.IWishService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class WishServiceImpl implements IWishService{
	
	private final IWishRepository wishRepository;
	private final IMemberRepository memberRepository;
	private final IBookRepository bookRepository;
	
	// 찜 등록
	@Override
	@Transactional
	public WishEntity addWished(Long bookId, Long memberId) throws Exception {
		MemberEntity memberEntity = memberRepository.findById(memberId)
								.orElseThrow(() -> new IllegalAccessException("NOT Member"));
		BookEntity bookEntity = bookRepository.findById(bookId)
								.orElseThrow(() -> new IllegalAccessException("NOT Book"));
		
		Optional<WishEntity> isWish = wishRepository.findByMemberEntityAndBookEntity(memberEntity, bookEntity);
		
		if (isWish.isPresent()) {
			log.warn("이미 찜 상태입니다.");
			throw new IllegalStateException("이미 찜 상태입니다.");
		}
		
		WishEntity wishEntity = WishEntity.builder().bookEntity(bookEntity)
													.memberEntity(memberEntity)
													.build();
		
		bookEntity.setWishCount(bookEntity.getWishCount() + 1);
		bookRepository.save(bookEntity);
		
		wishRepository.save(wishEntity);
		
		return wishEntity;
	}
	
	// 찜삭제
	@Override
	@Transactional
	public boolean removeWished(Long bookId, Long memberId) throws Exception {
		
		MemberEntity memberEntity = memberRepository.findById(memberId)
								.orElseThrow(() -> new IllegalAccessException("NOT Member"));
		BookEntity bookEntity = bookRepository.findById(bookId)
								.orElseThrow(() -> new IllegalAccessException("NOT Book"));
		
		Optional<WishEntity> existWished = wishRepository.findByMemberEntityAndBookEntity(memberEntity, bookEntity);
		
		if (existWished.isPresent()) {
			wishRepository.delete(existWished.get());
			bookEntity.setWishCount(bookEntity.getWishCount() - 1);
			bookRepository.save(bookEntity);
			
			return true;
		}
		return false;
	}
	
	// 찜 상태확인
	@Override
	public boolean isWished(Long bookId, Long memberId) throws Exception {
		MemberEntity memberEntity = memberRepository.findById(memberId)
				.orElseThrow(() -> new IllegalAccessException("Not member"));
		BookEntity bookEntity = bookRepository.findById(bookId)
				.orElseThrow(() -> new IllegalAccessException("Not book"));
		
		return wishRepository
				.findByMemberEntityAndBookEntity(memberEntity, bookEntity)
				.isPresent();
	}
	
	// 찜 목록
	@Override
	public Page<BookEntity> findWishListByMemberId(Long memberId, Pageable pageable) throws Exception {
		
		Page<WishEntity> wishPage = wishRepository.findByMemberEntity_MemberId(memberId, pageable);
		
		return wishPage.map(WishEntity::getBookEntity);
	}
	
}
