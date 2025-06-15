package com.codeit.mini.service.admin;

import java.util.Optional;

import com.codeit.mini.dto.AdminDTO;
import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.member.MemberEntity;


public interface IAdminService {

	// 관리자 회원가입 (가입 후 관리자 ID 반환)
		Long register(AdminDTO adminDTO);
		
		// 관리자 로그인 (로그인 ID와 비밀번호 체크 후 AdminDTO 반환 or Optional.empty())
	    Optional<AdminDTO> login(String adminLoginId, String adminPw);

	    // 관리자 정보 수정
	    void modify(AdminDTO adminDTO);

	    // 관리자 정보 조회 (ID로)
	    Optional<AdminDTO> getAdmin(Long adminId);
	    
	    // 목록 조회 + 검색 + 페이징
	 	PageResultDTO<MemberDTO, MemberEntity> getMemberList(PageRequestDTO requestDTO);
	 	
	 	default MemberDTO entityToDto(MemberEntity entity) {
	 		MemberDTO dto =  MemberDTO.builder()
	 					.memberId(entity.getMemberId())
	 					.loginId(entity.getLoginId())
	 					.memberName(entity.getMemberName())
	 					.memberEmail(entity.getMemberEmail())
	 					.emailVerified(entity.getEmailVerified())
	 					.regDate(entity.getRegDate())
	 					.updateDate(entity.getUpDate())
	 					.lastLogin(entity.getLastLogin())
	 					.build();
	 		return dto;
	 	}
	 	
	 	void removeMember(Long memberId);
	 	
	 	void updateMember(MemberDTO dto);
	 	
	 	Optional<MemberDTO> read(Long memberId);
}
