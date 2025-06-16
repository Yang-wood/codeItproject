package com.codeit.mini.repository.book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.RentEntity;
import com.codeit.mini.entity.member.MemberEntity;

@Repository
public interface IRentRepository extends JpaRepository<RentEntity, Long>{
	
	List<RentEntity> findByIsReturnedAndReturnDateLessThanEqual(Integer isReturned, LocalDateTime returnDate) throws Exception;
	
	Optional<RentEntity> findByBookEntityAndMemberEntityAndIsReturned(
						 BookEntity bookEntity,
						 MemberEntity memberEntity,
						 Integer isReturned) throws Exception;
	
	@Query(value = "SELECT r FROM RentEntity r WHERE r.memberEntity.memberId = :memberId",
	  countQuery = "SELECT count(r) FROM RentEntity r WHERE r.memberEntity.memberId = :memberId")
	Page<RentEntity> findByMemberEntity_MemberId(@Param("memberId") Long memberId, Pageable pageable) throws Exception;
}
