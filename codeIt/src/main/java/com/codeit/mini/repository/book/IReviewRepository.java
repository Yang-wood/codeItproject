package com.codeit.mini.repository.book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codeit.mini.dto.book.ReviewDTO;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.book.ReviewEntity;
import com.codeit.mini.entity.book.WishEntity;

@Repository
public interface IReviewRepository extends JpaRepository<ReviewEntity, Long> {
	
	// 별점 통계
	@Query("SELECT AVG(r.rating) "
		 + "FROM ReviewEntity r "
		 + "WHERE r.bookEntity.bookId = :bookId")
	Double findAvgRatingReviewByBookId(@Param("bookId") Long bookId) throws Exception;
	
	// 대여건에 대한 리뷰 유무
	@Query("SELECT r "
		 + "FROM ReviewEntity r "
		 + "WHERE r.rentEntity.rentId = :rentId")
	Optional<ReviewEntity> findByRentId(@Param("rentId") Long rentId) throws Exception;
	
	//  특정 도서에 대한 리뷰 리스트
	@Query("SELECT r "
			+ "FROM ReviewEntity r "
			+ "WHERE r.bookEntity.bookId = :bookId")
	Page<ReviewEntity> findAllByBookId(@Param("bookId") Long bookId, Pageable pageable) throws Exception;
	
	// 특정 도서에 대한 특정 회원의 리뷰 리스트
	@Query("SELECT r "
			+ "FROM ReviewEntity r "
			+ "WHERE r.bookEntity.bookId = :bookId "
			+ "AND r.memberEntity.memberId = :memberId")
	List<ReviewEntity> findAllByBookIdAndMemberId(@Param("bookId") Long bookId, @Param("memberId") Long memberId ) throws Exception;
	
	
}
