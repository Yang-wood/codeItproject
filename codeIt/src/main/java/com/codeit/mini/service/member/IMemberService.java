package com.codeit.mini.service.member;

import java.util.Optional;

import com.codeit.mini.dto.comm.PageRequestDTO;
import com.codeit.mini.dto.comm.PageResultDTO;
import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.member.MemberEntity;


public interface IMemberService {

	Long register(MemberDTO dto);
	
	// 목록 조회 + 검색 + 페이징
	PageResultDTO<MemberDTO, MemberEntity> getList(PageRequestDTO pageRequestDTO);
	
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
	
	void modify(MemberDTO dto);
	
	void remove(Long memberId);
	
	Optional<MemberDTO> login(String loginId, String rawPw);
	
	Optional<MemberDTO> read(Long memberId);
}
