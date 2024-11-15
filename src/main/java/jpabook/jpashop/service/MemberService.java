package jpabook.jpashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@Service
@Transactional(readOnly = true) // 읽기 성능 향상, 조회가 많으면 이렇게 하고 등록/수정쪽엔 별도로 어노테이션을 넣어주자
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	// 회원가입
	@Transactional
	public Long join(Member member) {
		// 중복 회원 제거
		validateDuplidateMember(member);
		memberRepository.save(member);
		
		return member.getId();
	}
	private void validateDuplidateMember(Member member) {
		List<Member> findMembers = memberRepository.findByNames(member.getName());
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
		
	}
	// 회원 전체 조회
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	// 회원 단일 조회
	public Member findMember(Long id){
		return memberRepository.findOne(id);
	}
	
	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findOne(id);
		member.setName(name);
	}
	
}
