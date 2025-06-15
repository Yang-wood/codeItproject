package com.codeit.mini.repository.book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.WishEntity;
import com.codeit.mini.entity.member.MemberEntity;

@Repository
public interface IWishRepository extends JpaRepository<WishEntity, Long> {
	
	List<WishEntity> findAllByMemberEntity(MemberEntity memberEntity) throws Exception;
	
	Optional<WishEntity> findByMemberEntityAndBookEntity(MemberEntity memberEntity,
														 BookEntity bookEntity) throws Exception;
	
	Page<WishEntity> findByMemberEntity_MemberId(Long memberId, Pageable pageable) throws Exception;
}
