package com.codeit.mini.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.member.EmailAuthEntity;

public interface EmailAuthRepository extends JpaRepository<EmailAuthEntity, String>{

}
