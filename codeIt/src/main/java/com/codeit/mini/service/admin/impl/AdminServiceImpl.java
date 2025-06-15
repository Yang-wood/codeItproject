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

        if (type != null && keyword != null) {
            if (type.contains("i")) {
                builder.or(qMember.loginId.contains(keyword));
            }
            if (type.contains("e")) {
                builder.or(qMember.memberEmail.contains(keyword));
            }
            if (type.contains("n")) {
                builder.or(qMember.memberName.contains(keyword));
            }
        }

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
            entity.changeName(dto.getMemberName());
            entity.changeEmail(dto.getMemberEmail());
            entity.changepw(dto.getMemberPw());
            memberRepository.save(entity);
        }
		
	}
	@Override
	public Optional<MemberDTO> read(Long memberId) {
		
		return memberRepository.findById(memberId)
	            .map(this::entityToDto);
	}

}
