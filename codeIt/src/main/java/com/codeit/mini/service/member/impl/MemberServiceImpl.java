package com.codeit.mini.service.member.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.mini.dto.member.MemberDTO;
import com.codeit.mini.entity.member.MemberEntity;
import com.codeit.mini.repository.book.IRentRepository;
import com.codeit.mini.repository.book.IWishRepository;
import com.codeit.mini.repository.member.IMemberRepository;
import com.codeit.mini.service.member.IMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements IMemberService{
	
	private final IMemberRepository memberRepository;
	
	private final IRentRepository rentRepository;
	
	private final IWishRepository wishRepository;
	
	private final PasswordEncoder passwordEncoder;
	@Override
	public Long register(MemberDTO dto) {
		
		String rawPw = dto.getMemberPw();
		String encPw = passwordEncoder.encode(rawPw);
		
		MemberEntity memberEntity = MemberEntity.builder()
										.loginId(dto.getLoginId())
										.memberPw(encPw) // 암호화된 비밀번호 저장
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

	        if (dto.getMemberName() != null)
	            entity.changeName(dto.getMemberName());

	        if (dto.getMemberEmail() != null)
	            entity.changeEmail(dto.getMemberEmail());

	        if (dto.getMemberPw() != null && !dto.getMemberPw().isBlank()) {
	            String encPw = passwordEncoder.encode(dto.getMemberPw());
	            entity.changepw(encPw);
	        }

	        memberRepository.save(entity);
	    }
		
	}

	@Override
	public Optional<MemberDTO> login(String loginId, String rawPw) {
		
		Optional<MemberEntity> result = memberRepository.findByLoginId(loginId);

	    if (result.isPresent()) {
	        MemberEntity member = result.get();

	        // status가 2(탈퇴)인 경우 로그인 차단
	        if (member.getStatus() == 2) {
	            return Optional.empty(); // 탈퇴한 계정은 로그인 불가
	        }
	        
	        if (passwordEncoder.matches(rawPw, member.getMemberPw())) {
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
                .termsAgreed(entity.getTermsAgreed())
                .status(entity.getStatus())
                .role(entity.getRole())
                .points(entity.getPoints())
                .regDate(entity.getRegDate())
                .updateDate(entity.getUpDate())
                .lastLogin(entity.getLastLogin())
                .build();
    }

	@Override
	@Transactional
	public void delete(Long memberId) {
		
		Optional<MemberEntity> result = memberRepository.findById(memberId);
	    result.ifPresent(entity -> {
	        entity.setStatus(2); // 예: 2 = withdrawn
	        memberRepository.save(entity);
	    });
		
	}

	@Override
	public int getRentCount(Long memberId) {
		
		return rentRepository.countByMemberEntity_MemberId(memberId);
	}

	@Override
	public int getWishCount(Long memberId) {
		
		return wishRepository.countByMemberEntity_MemberId(memberId);
	}

	
}
