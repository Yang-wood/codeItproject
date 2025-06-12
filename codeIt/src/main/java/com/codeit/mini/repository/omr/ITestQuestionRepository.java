package com.codeit.mini.repository.omr;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.omr.TestQuestionEntity;

public interface ITestQuestionRepository extends JpaRepository<TestQuestionEntity, Long>{

}
