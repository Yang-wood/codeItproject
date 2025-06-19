package com.codeit.mini.repository.vending.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.entity.comm.VendingType;
import com.codeit.mini.entity.vending.QVendingMachinesEntity;
import com.codeit.mini.entity.vending.VendingMachinesEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

@Repository
public class VendingMachinesSearchImpl extends QuerydslRepositorySupport implements IVendingMachinesSearch{

	public VendingMachinesSearchImpl() {
		super(VendingMachinesEntity.class);
	}

	@Override
	public Page<VendingMachinesEntity> searchMachines(PageRequestDTO requestDTO, Pageable pageable) {
	    QVendingMachinesEntity vm = QVendingMachinesEntity.vendingMachinesEntity;

	    JPQLQuery<VendingMachinesEntity> query = from(vm);

	    BooleanBuilder builder = new BooleanBuilder();

	    if (requestDTO.getKeyword() != null && !requestDTO.getKeyword().isBlank()) {
	        builder.and(vm.name.containsIgnoreCase(requestDTO.getKeyword()));
	    }

	    if (requestDTO.getType() != null && !requestDTO.getType().isBlank()) {
	        try {
	            builder.and(vm.type.eq(VendingType.valueOf(requestDTO.getType())));
	            
	        } catch (IllegalArgumentException e) {
	            throw new RuntimeException("잘못된 자판기 타입입니다: " + requestDTO.getType());
	        }
	    }

	    query.where(builder);
	    query.orderBy(vm.regDate.desc());
	    this.getQuerydsl().applyPagination(pageable, query);

	    List<VendingMachinesEntity> content = query.fetch();
	    long total = query.fetchCount();

	    return new PageImpl<>(content, pageable, total);
	}

}
