package com.codeit.mini.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.member.MemberEntity;

public interface IMemberRepository extends JpaRepository<MemberEntity, Long>{

}
