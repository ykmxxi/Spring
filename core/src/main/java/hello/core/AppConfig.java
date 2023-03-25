package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class AppConfig {

	public MemberService memberService() {
		// 생성자 주입
		return new MemberServiceImpl(memberRepository());
	}

	private static MemoryMemberRepository memberRepository() {
		return new MemoryMemberRepository(); // 다른 구현체로 변경할 때 여기만 변경
	}

	public OrderService orderService() {
		// 생성자 주입
		return new OrderServiceImpl(memberRepository(), discountPolicy());
	}

	public DiscountPolicy discountPolicy() {
		return new FixDiscountPolicy(); // 다른 구현체로 변경할 때 여기만 변경
	}

}
