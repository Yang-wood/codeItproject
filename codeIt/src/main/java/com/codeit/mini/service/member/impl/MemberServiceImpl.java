package com.codeit.mini.service.member.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.service.member.IMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements IMemberService{
	
	private final IMemberRepository memberRepository;
	
	@Override
	public Long register(MemberDTO dto) {
		
		MemberEntity memberEntity = MemberEntity.builder()
										.loginId(dto.getLoginId())
										.memberPw(dto.getMemberPw())
										.memberName(dto.getMemberName())
										.memberEmail(dto.getMemberEmail())
										.termsAgreed(dto.getTermsAgreed())
										.emailVerified('N')
										.build();
		memberRepository.save(memberEntity);
		
		return memberEntity.getMemberId();
	}

	@Override
	@Transactional
	public void modify(MemberDTO dto) {
		
		Optional<MemberEntity> result = memberRepository.findById(dto.getMemberId());
		
		if (result.isPresent()) {
			MemberEntity entity = result.get();
			entity.changeName(dto.getMemberName());
			entity.changeEmail(dto.getMemberEmail());
			entity.changepw(dto.getMemberPw());
            
			memberRepository.save(entity);
		}
		
	}

	@Override
	public Optional<MemberDTO> login(String loginId, String rawPw) {
		
		Optional<MemberEntity> result = memberRepository.findByLoginId(loginId);

	    if (result.isPresent()) {
	        MemberEntity member = result.get();

	        if (rawPw.equals(member.getMemberPw())) {
	        	member.updateLastLogin();  // 로그인 시각 갱신
	            memberRepository.save(member);  // 변경 내용 저장
	            return Optional.of(entityToDto(member));
	        }
	    }

	    return Optional.empty();
	}

	@Override
	public Optional<MemberDTO> read(Long memberId) {
		
		return memberRepository.findById(memberId).map(this::entityToDto);
	}
	
	private MemberDTO entityToDto(MemberEntity entity) {
		
        return MemberDTO.builder()
                .memberId(entity.getMemberId())
                .loginId(entity.getLoginId())
                .memberName(entity.getMemberName())
                .memberEmail(entity.getMemberEmail())
                .emailVerified(entity.getEmailVerified())
                .regDate(entity.getRegDate())
                .updateDate(entity.getUpDate())
                .lastLogin(entity.getLastLogin())
                .build();
    }

	
}
