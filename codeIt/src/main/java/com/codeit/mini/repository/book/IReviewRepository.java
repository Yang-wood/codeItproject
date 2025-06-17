package com.codeit.mini.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codeit.mini.entity.book.ReviewEntity;

@Repository
public interface IReviewRepository extends JpaRepository<ReviewEntity, Long> {
	
	@Query("SELECT AVG(r.rating) "
		 + "FROM ReviewEntity r "
		 + "WHERE r.bookEntity.bookId = :bookId")
	Double findAvgRatingReviewByBookId(@Param("bookId") Long bookId);
	
		
}
