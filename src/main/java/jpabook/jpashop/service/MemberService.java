package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// JPA 에서의 변경로직은 트랜젝션 안에서 이루어저야 LAZY 전략 등이 동작한다.
// 여기서는 읽는 메서드가 더 많아 readOnly = true를 전역설정으로 두고 수정 메서드는 설정을 오버라이딩했다.
@Transactional(readOnly = true)
@RequiredArgsConstructor    // final 붙은 필드만 생성자로 추가
public class MemberService {

    // final 추가 추천
    private final MemberRepository memberRepository;

  /** 생성자 1개 + @RequiredArgsConstructor 를 사용해 생략가능
    // 생성자 1개 -> 생략가능
    // @Autowired
    // public MemberService(MemberRepository memberRepository) {
    //     this.memberRepository = memberRepository;
    // }
  */

    /**
     *  회원 가입
     */
    @Transactional(readOnly = false)
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        // persist시 jpa가 메모리상에서 id를 계산해 넣어주기에 id값이 들어있다.
        return member.getId();
    }

    private Boolean validateDuplicateMember(Member member) {
        // 이름으로 중복관리
        // 쓰레드로 인해 동시에 호출 시 문제가 있는 코드이다. 그래서 테이블의 name이 UNIQUE 설정이 돼어 있어야 한다.
        List<Member> foundMembers = memberRepository.findByName(member.getName());

        if(!foundMembers.isEmpty()) throw new IllegalStateException("이미 존재하는 회원.");
        else return true;
    }

    // 회원 전체 조회
    public List<Member> foundMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
