package com.codeit.mini.service.book.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.PointHistoryEntity;
import com.codeit.mini.repository.book.IBookRepository;
import com.codeit.mini.repository.book.IRentRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.IPointHistoryRepository;
import com.codeit.mini.service.book.IRentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class RentServiceImpl implements IRentService {
	
	private final IRentRepository rentRepository;
	private final IMemberRepository memberRepository;
	private final IBookRepository bookRepository;
	private final IPointHistoryRepository pointRepository;
	
	// 대여 등록
	@Override
	@Transactional
	public RentEntity rentBook(Long bookId, Long memberId) throws Exception {
		MemberEntity memberEntity = memberRepository.findById(memberId)
							  .orElseThrow(() -> new IllegalAccessException("memInfo : No"));
		BookEntity bookEntity = bookRepository.findById(bookId)
							  .orElseThrow(() -> new IllegalAccessException("bookInfo : No"));
		
		
		int rentPoint = bookEntity.getRentPoint();
		
		if (memberEntity.getPoints() < rentPoint) {
			throw new Exception("보유 포인트가 부족합니다.");
		}
		
		memberEntity.setPoints(memberEntity.getPoints() - rentPoint);
		memberRepository.save(memberEntity);
		
		PointHistoryEntity historyEntity = PointHistoryEntity.builder().memberId(memberEntity)
																	   .amount(-rentPoint)
																	   .type("use")
																	   .reason("도서 대여")
																	   .build();
		
		pointRepository.save(historyEntity);
		
		Optional<RentEntity> isRent = rentRepository.findByBookEntityAndMemberEntityAndIsReturned(bookEntity, memberEntity, 0);
		log.info("bookEntity : {}", bookEntity);
		log.info("memberEntity : {}", memberEntity);
		log.info("rent : {}", isRent);
		
		if (isRent.isPresent()) {
			log.warn("이미 대여중입니다.");
			throw new IllegalStateException("이미 대여중입니다.");
		}
		
		LocalDateTime now = LocalDateTime.now();
        LocalDateTime returnDate = now.plusDays(7);
        
        RentEntity rentEntity = RentEntity.builder().bookEntity(bookEntity)
        											.memberEntity(memberEntity)
        											.rentDate(now)
        											.returnDate(returnDate)
        											.isReturned(0)
        											.readPage(0)
        											.readState(0)
        											.hasReview(0)
        											.pointGet(0)
        											.build();
        // 대여 횟수 증가식
		bookEntity.setRentCount(bookEntity.getRentCount() + 1);
		log.info("rentInfo : {}",rentEntity);
		rentRepository.save(rentEntity);
        
        return rentEntity;
	}
	
	// 반납 상태확인
	@Transactional
	public boolean isRented(Long bookId, Long memberId) throws Exception {
		Optional<BookEntity> bookOptional = bookRepository.findById(bookId);
		Optional<MemberEntity> memberOptional = memberRepository.findById(memberId);
		
		if (bookOptional.isEmpty() || memberOptional.isEmpty()) {
			return false;
		}
		
		return rentRepository
				.findByBookEntityAndMemberEntityAndIsReturned(
						bookOptional.get(), 
						memberOptional.get(), 0).isPresent();
	}
	
	// 자동 반납
	@Override
	@Transactional
	@Scheduled(cron = "0 0 12 * * ?")
	public void rentEndList() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		
		List<RentEntity> expiredRent = rentRepository.findByIsReturnedAndReturnDateLessThanEqual(0, now);
	
		for (RentEntity rentEntity : expiredRent) {
			rentEntity.setIsReturned(1);
			
			BookEntity bookEntity = rentEntity.getBookEntity();
			if (bookEntity != null) {
				bookEntity.setRentCount(bookEntity.getRentCount() - 1);
				bookRepository.save(bookEntity);
			} else {
				log.warn("NOT BOOK");
				throw new IllegalAccessException("NOT BOOK");
			}
			
			rentRepository.save(rentEntity);
			log.info("반납처리 rentId : {}", rentEntity.getRentId());
		}
	}
	
	// 대여 목록
	@Override
	public Page<RentEntity> findRentListByMemberId(Long memberId, Pageable pageable) throws Exception {
		
		Page<RentEntity> rentPage = rentRepository.findByMemberEntity_MemberId(memberId, pageable);
		
		return rentPage;
	}
}
