package com.codeit.mini.repository.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.codeit.mini.entity.member.MemberEntity;

public interface IMemberRepository extends JpaRepository<MemberEntity, Long>, QuerydslPredicateExecutor<MemberEntity>{

	Optional<MemberEntity> findByLoginId(String loginId);
}
