package com.codeit.mini.repository.book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.book.WishEntity;
import com.codeit.mini.entity.member.MemberEntity;

@Repository
public interface IRentRepository extends JpaRepository<RentEntity, Long>{
	
	List<RentEntity> findByIsReturnedAndReturnDateLessThanEqual(Integer isReturned, LocalDateTime returnDate);
	
	Optional<RentEntity> findByBookEntityAndMemberEntityAndIsReturned(
						 BookEntity bookEntity,
						 MemberEntity memberEntity,
						 Integer isReturned);
	
	

}
