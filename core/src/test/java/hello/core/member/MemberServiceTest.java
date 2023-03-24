package hello.core.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberServiceTest {

	MemberService memberService = new MemberServiceImpl();

	@DisplayName("회원가입 기능")
	@Test
	void givenMember_whenJoin_thenStoreMember() {
		// given
		Member member = new Member(1L, "memberA", Grade.VIP);

		// when
		memberService.join(member);

		// then
		assertThat(member).isEqualTo(memberService.findMember(1L));
	}

}
