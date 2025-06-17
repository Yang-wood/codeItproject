package com.codeit.mini.service.book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codeit.mini.dto.book.ReviewDTO;
import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.book.ReviewEntity;
import com.codeit.mini.entity.member.MemberEntity;

public interface IReviewService {
	
	// 리뷰 등록
	ReviewEntity regReview(ReviewDTO reviewDTO) throws Exception;
	
	// 리뷰 삭제
	void removeReview(Long reviewId) throws Exception;
	
	// 리뷰 수정
	void modifyReview(ReviewDTO reviewDTO) throws Exception;
	
	// 리뷰 유무
	boolean hasReview(Long rentId) throws Exception;
	
	// 본인 리뷰인지 학인
	ReviewDTO findById(Long reviewId) throws Exception;
	
	// 특정 도서 전체 회원 리뷰 목록
	Page<ReviewEntity> findReviewByBookId(Long bookId, Pageable pageable) throws Exception;
	
	// 특정 도서 + 특정 회원 리뷰 목록
	List<ReviewDTO> findReviewByMemberId(Long bookId, Long MemberId) throws Exception;
	
	default ReviewDTO entityToDto(ReviewEntity reviewEntity) {
		ReviewDTO reviewDTO = ReviewDTO.builder().reviewId(reviewEntity.getReviewId())
												 .memberId(reviewEntity.getMemberEntity().getMemberId())
												 .bookId(reviewEntity.getBookEntity().getBookId())
												 .title(reviewEntity.getTitle())
												 .content(reviewEntity.getContent())
												 .rating(reviewEntity.getRating())
												 .regdate(reviewEntity.getRegDate())
												 .updateDate(reviewEntity.getUpDate())
												 .build();
		return reviewDTO;
	}
	
	default ReviewEntity dtoToEntity(ReviewDTO reviewDTO) {
		ReviewEntity reviewEntity = ReviewEntity.builder().reviewId(reviewDTO.getReviewId())
												.memberEntity(MemberEntity.builder().memberId(reviewDTO.getMemberId()).build())
												.rentEntity(RentEntity.builder().rentId(reviewDTO.getRentId()).build())
												.bookEntity(BookEntity.builder().bookId(reviewDTO.getBookId()).build())
												.title(reviewDTO.getTitle())
												.content(reviewDTO.getContent())
												.rating(reviewDTO.getRating())
												.build();
		
		return reviewEntity;
												
														  
	}
	
}
