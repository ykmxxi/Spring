package hello.core.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

class OrderServiceTest {

	MemberService memberService = new MemberServiceImpl();
	OrderService orderService = new OrderServiceImpl();

	@DisplayName("주문 생성: VIP 회원의 고정 할인")
	@Test
	void givenVipMember_whenCreateOrder_thenReturnsCorrectDiscountPrice() {
		// given
		Long memberId = 1L;
		Member member = new Member(memberId, "memberA", Grade.VIP);
		memberService.join(member);

		// when
		Order order = orderService.createOrder(memberId, "itemA", 10000);

		// then
		assertThat(order.getDiscountPrice()).isEqualTo(1000);
	}

}
