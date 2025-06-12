package com.codeit.mini.repository.omr;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.omr.TestSessionEntity;

public interface ITestSessionRepository extends JpaRepository<TestSessionEntity, Long>{

}
