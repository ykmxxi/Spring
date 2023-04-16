package hello.core.autowired;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import hello.core.member.Member;

public class AutowiredTest {

	@Test
	void AutowiredOption() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

	}

	@Component
	static class TestBean {

		// Member 클래스는 스프링 빈이 아님!!!
		// 호출 안됨
		@Autowired(required = false)
		public void setNoBean1(Member member) {
			System.out.println("noBean1 = " + member);
		}

		//null 호출
		@Autowired
		public void setNoBean2(@Nullable Member member) {
			System.out.println("noBean2 = " + member);
		}

		//Optional.empty 호출
		@Autowired
		public void setNoBean3(Optional<Member> member) {
			System.out.println("noBean3 = " + member);
		}

		/*
		setNoBean1()은 @Autowired(required = false)이므로 호출 자체가 안됨
		setNoBean2 = null
		setNoBean3 = Optional.empty
		*/
	}

}
