package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }


    @Test
    void 회원가입() {
        Member member = new Member();
        member.setName("choi");

        Long savedId = memberService.signup(member);

        Member findMember = memberService.findMembers(savedId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복확인() {
        Member member1 = new Member();
        member1.setName("member1");

        Member member2 = new Member();
        member2.setName("member1");

        memberService.signup(member1);
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> memberService.signup(member2));

        assertThat(illegalStateException.getMessage()).isEqualTo("이미 존재하는 유저");

//        try{
//            memberService.signup(member2);
//            fail();
//        }catch (IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 유저");
//        }
    }

    @Test
    void 회원찾기() {
    }

    @Test
    void testFindMembers() {
    }
}