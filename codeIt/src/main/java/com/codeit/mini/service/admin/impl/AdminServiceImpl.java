package com.codeit.mini.service.admin.impl;

import java.util.Optional;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.mini.dto.AdminDTO;
import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.entity.member.QMemberEntity;
import com.codeit.mini.repository.IAdminRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.service.admin.IAdminService;
import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService{
	
	private final IAdminRepository adminRepository;
	private final IMemberRepository memberRepository;
	
	@Override
	public Long register(AdminDTO adminDTO) {
		// 비밀번호 암호화 필요
        
        Admin admin = Admin.builder()
				        		.adminLoginId(adminDTO.getAdminLoginId())
				        		.adminPw(adminDTO.getAdminPw())
				                .adminName(adminDTO.getAdminName())
				                .adminEmail(adminDTO.getAdminEmail())
				                .build();
        Admin savedAdmin = adminRepository.save(admin);
		return savedAdmin.getAdminId();
	}
	
	@Override
	public Optional<AdminDTO> login(String adminLoginId, String adminPw) {
		
		Optional<Admin> adminOpt = adminRepository.findByAdminLoginId(adminLoginId);
        
		if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (adminPw.equals(admin.getAdminPw())) {
                return Optional.of(AdminDTO.builder()
                        .adminId(admin.getAdminId())
                        .adminLoginId(admin.getAdminLoginId())
                        .adminName(admin.getAdminName())
                        .adminEmail(admin.getAdminEmail())
                        .build());
            }
        }
        return Optional.empty();
	}
	
	@Override
	@Transactional
	public void modify(AdminDTO adminDTO) {
		
		adminRepository.findById(adminDTO.getAdminId()).ifPresent(admin -> {
            admin.setAdminName(adminDTO.getAdminName());
            admin.setAdminEmail(adminDTO.getAdminEmail());
            if (adminDTO.getAdminPw() != null && !adminDTO.getAdminPw().isEmpty()) {
                admin.setAdminPw(adminDTO.getAdminPw());
            }
            adminRepository.save(admin);
        });
		
	}
	
	@Override
	public Optional<AdminDTO> getAdmin(Long adminId) {
		
		return adminRepository.findById(adminId)
                .map(admin -> AdminDTO.builder()
                        .adminId(admin.getAdminId())
                        .adminLoginId(admin.getAdminLoginId())
                        .adminName(admin.getAdminName())
                        .adminEmail(admin.getAdminEmail())
                        .build());
	}
	
	@Override
	public PageResultDTO<MemberDTO, MemberEntity> getMemberList(PageRequestDTO requestDTO) {
		
		Pageable pageable = requestDTO.getPageable(Sort.by("memberId").descending());

        // 검색 조건 builder
        BooleanBuilder builder = getSearch(requestDTO);

        // repository에서 조건 + 페이징 조회
        Page<MemberEntity> result = memberRepository.findAll(builder, pageable);

        // entity → dto 변환 함수
        Function<MemberEntity, MemberDTO> fn = entity -> entityToDto(entity);

        return new PageResultDTO<>(result, fn);
	}
	
	private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
       
		String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();
        BooleanBuilder builder = new BooleanBuilder();
        QMemberEntity qMember = QMemberEntity.memberEntity;
        
     // 검색어가 비어있으면 전체 검색
        if (keyword == null || keyword.trim().isEmpty()) {
            return builder;
        }

        BooleanBuilder condition = new BooleanBuilder();

        if (type == null || type.isBlank()) {
            // 전체 검색: 이름, 아이디, 이메일 중 하나라도 포함되면
            condition.or(qMember.memberName.containsIgnoreCase(keyword));
            condition.or(qMember.loginId.containsIgnoreCase(keyword));
            condition.or(qMember.memberEmail.containsIgnoreCase(keyword));
        } else {
            if (type.contains("n")) {
                condition.or(qMember.memberName.containsIgnoreCase(keyword));
            }
            if (type.contains("i")) {
                condition.or(qMember.loginId.containsIgnoreCase(keyword));
            }
            if (type.contains("e")) {
                condition.or(qMember.memberEmail.containsIgnoreCase(keyword));
            }
            
            builder.and(condition);
        }
        

        builder.and(condition);
        return builder;
    }
	
	@Override
	@Transactional
	public void removeMember(Long memberId) {
		
		Optional<MemberEntity> result = memberRepository.findById(memberId);
	    result.ifPresent(entity -> {
	        entity.setStatus(2); // 소프트 삭제
	        memberRepository.save(entity);
	    });
		
	}
	
	@Override
	@Transactional
	public void updateMember(MemberDTO dto) {
	    Optional<MemberEntity> optional = memberRepository.findById(dto.getMemberId());

	    if (optional.isPresent()) {
	        MemberEntity entity = optional.get();

	        // 이름, 이메일, 비밀번호는 수정하지 않음
	        entity.changeStatus(dto.getStatus());
	        entity.changeRole(dto.getRole());
	        entity.changePoints(dto.getPoints());

	        memberRepository.save(entity);
	    }
	}
	
	@Override
	public Optional<MemberDTO> read(Long memberId) {
		
		return memberRepository.findById(memberId)
	            .map(this::entityToDto);
	}

	@Override
	public Long getAdminId(String loginId) {
		
		
		return null;
	}

}
