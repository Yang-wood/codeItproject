package com.codeit.mini.service.member;

import java.util.Optional;


import com.codeit.mini.dto.member.MemberDTO;


public interface IMemberService {

Long register(MemberDTO dto);
	
	void modify(MemberDTO dto);
	
	Optional<MemberDTO> login(String loginId, String rawPw);
	
	Optional<MemberDTO> read(Long memberId);
}
