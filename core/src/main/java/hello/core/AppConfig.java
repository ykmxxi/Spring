package hello.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

@Configuration // 애플리케이션의 설정 정보(구성 정보)
public class AppConfig {

	@Bean
	public MemberService memberService() {
		// 생성자 주입
		System.out.println("call AppConfig.memberService");
		return new MemberServiceImpl(memberRepository());
	}

	@Bean
	public MemberRepository memberRepository() {
		System.out.println("call AppConfig.memberRepository");
		return new MemoryMemberRepository(); // 다른 구현체로 변경할 때 여기만 변경
	}

	@Bean
	public OrderService orderService() {
		// 생성자 주입
		System.out.println("call AppConfig.orderService");
		return new OrderServiceImpl(memberRepository(), discountPolicy());
	}

	@Bean
	public DiscountPolicy discountPolicy() {
//		return new FixDiscountPolicy(); // 다른 구현체로 변경할 때 여기만 변경
		return new RateDiscountPolicy();
	}

}
