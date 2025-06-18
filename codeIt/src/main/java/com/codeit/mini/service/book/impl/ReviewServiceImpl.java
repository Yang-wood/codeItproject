package com.codeit.mini.service.book.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.mini.dto.book.ReviewDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.book.ReviewEntity;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.vending.PointHistoryEntity;
import com.codeit.mini.repository.book.IBookRepository;
import com.codeit.mini.repository.book.IRentRepository;
import com.codeit.mini.repository.book.IReviewRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.repository.vending.IPointHistoryRepository;
import com.codeit.mini.service.book.IReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService{
	
	private final IReviewRepository reviewRepository;
	private final IRentRepository rentRepository;
	private final IBookRepository bookRepository;
	private final IPointHistoryRepository historyRepository;
	private final IMemberRepository memberRepository;
	
	// 평균 별점 계산식
	private void updateAvgRating(Long bookId) {
		try {
			Double avgRating = reviewRepository.findAvgRatingReviewByBookId(bookId);
			
			if (avgRating == null) {
				avgRating = 0.0;
			}
			
			BookEntity bookEntity = bookRepository.findById(bookId)
					.orElseThrow(() -> new IllegalAccessException("NOT BOOK"));
			bookEntity.setAvgRating(avgRating);
			bookRepository.save(bookEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 리뷰 등록
	@Override
	@Transactional
	public Map<String, Object> regReview(ReviewDTO reviewDTO) throws Exception {
		
		Map<String, Object> result = new HashMap<>();
		
		MemberEntity memberEntity = memberRepository.findById(reviewDTO.getMemberId())
				.orElseThrow(() -> new IllegalAccessException("NOT MEMBER"));
		RentEntity rent = rentRepository.findById(reviewDTO.getRentId())
				.orElseThrow(() -> new IllegalAccessException("Not RentId"));
		log.info("reviewDTO.getRentId : {}", reviewDTO.getRentId());
		
		if (rent.getHasReview() != null && rent.getHasReview() == 1) {
			throw new IllegalStateException("이미 리뷰가 있습니다.");
		}
		
		ReviewEntity reviewEntity = dtoToEntity(reviewDTO);
		ReviewEntity saveReview = reviewRepository.save(reviewEntity);
		
		BookEntity bookEntity = rent.getBookEntity();
		bookEntity.setReviewCount(bookEntity.getReviewCount() + 1);
		bookRepository.save(bookEntity);
		
		rent.setHasReview(1);
		rentRepository.save(rent);
		
		boolean pointGiven = false;
		
		if (rent.getPointGet() == 0) {
			PointHistoryEntity historyEntity = PointHistoryEntity.builder().memberId(memberEntity)
																		   .amount(+50)
																		   .type("charge")
																		   .reason("리뷰 등록")
																		   .build();
			historyRepository.save(historyEntity);
			rent.setPointGet(1);
			rentRepository.save(rent);
			
			pointGiven = true;
		}
		
		updateAvgRating(reviewDTO.getBookId());
		
		result.put("message", "리뷰 등록 완료");
		result.put("pointGiven", pointGiven);
		result.put("reviewId", saveReview.getReviewId());
		
		return result;
	}
	
	// 리뷰 삭제
	@Override
	@Transactional
	public void removeReview(Long reviewId) throws Exception {
		
		ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalAccessException("Not ReviewId"));
				
		RentEntity rent = reviewEntity.getRentEntity();
		if (rent == null) {
			throw new IllegalStateException("Not Rent");
		}
		
		Long bookIdUpdate = reviewEntity.getBookEntity().getBookId();
		
		reviewRepository.delete(reviewEntity);
		
		BookEntity bookEntity = rent.getBookEntity();
		bookEntity.setReviewCount(bookEntity.getReviewCount() - 1);
		
		rent.setHasReview(0);
		rentRepository.save(rent);
		
		updateAvgRating(bookIdUpdate);
		
	}
	
	// 리뷰 수정
	@Override
	@Transactional
	public void modifyReview(ReviewDTO reviewDTO) throws Exception {
		
		ReviewEntity review = reviewRepository.findById(reviewDTO.getReviewId())
				.orElseThrow(() -> new IllegalAccessException("NOT REVIEW"));
		
		review.setTitle(reviewDTO.getTitle());
		review.setContent(reviewDTO.getContent());
		review.setRating(reviewDTO.getRating());
		
		reviewRepository.save(review);
		
		updateAvgRating(review.getBookEntity().getBookId());
	}
	
	// 리뷰 유무 판단
	@Override
	public boolean hasReview(Long rentId) throws Exception {
		log.info("ReviewService.hasReview called with rentId: {}", rentId);
		Optional<ReviewEntity> reviewOptional = reviewRepository.findByRentId(rentId);
		boolean result = reviewOptional.isPresent();
		log.info("Review for rentId {} is present: {}", rentId, result);
		return reviewOptional.isPresent();
	}
	
	// 특정 책에 대한 ALL 리뷰 -  도서 상세
	@Override
	public Page<ReviewEntity> findReviewByBookId(Long bookId, Pageable pageable) throws Exception {
		
		return reviewRepository.findAllByBookId(bookId, pageable);
	}

	// 특정 책에 대한 특정 회원의 ALL 리뷰 - my page : 대여 목록
	@Override
	public List<ReviewDTO> findReviewByMemberId(Long bookId, Long MemberId) throws Exception {
		List<ReviewEntity> reviews = reviewRepository.findAllByBookIdAndMemberId(bookId, MemberId);
		
		return reviews.stream().map(this::entityToDto).collect(Collectors.toList());
	}
	
	// 본인 리뷰인지 판단
	@Override
	public ReviewDTO findById(Long reviewId) throws Exception {
		
		return reviewRepository.findById(reviewId).map(this::entityToDto).orElse(null);
	}
	
	
}
