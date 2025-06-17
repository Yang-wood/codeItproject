package com.codeit.mini.repository.vending;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.vending.PointHistoryEntity;

public interface IPointHistoryRepository extends JpaRepository<PointHistoryEntity, Long>{
	Page<PointHistoryEntity> findByMemberId_MemberId(Long memberId, Pageable pageable);
}
