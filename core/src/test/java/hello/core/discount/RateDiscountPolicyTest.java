package hello.core.discount;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hello.core.member.Grade;
import hello.core.member.Member;

class RateDiscountPolicyTest {

	RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

	@DisplayName("VIP는 10% 할인이 적용되어야 한다")
	@Test
	void givenVipMember_whenRateDiscount_thenReturnsTenPercentDiscount() {
		// given
		Member member = new Member(1L, "memberVip", Grade.VIP);

		// when
		int discount = discountPolicy.discount(member, 10000);

		// then
		assertThat(discount).isEqualTo(1000);
	}

	@DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다")
	@Test
	void givenBasicMember_whenRateDiscount_thenReturnsZero() {
		// given
		Member member = new Member(2L, "memberBasic", Grade.BASIC);

		// when
		int discount = discountPolicy.discount(member, 10000);

		// then
		assertThat(discount).isEqualTo(0);
	}

}
