package hello.core.singleton;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

	@Test
	void statefulServiceSingleton() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

		StatefulService statefulService1 = ac.getBean(StatefulService.class);
		StatefulService statefulService2 = ac.getBean(StatefulService.class);

		// ThreadA: A 사용자가 10000원 주문
		statefulService1.order("userA", 10000);

		// ThreadB: B 사용자가 20000원 주문
		statefulService2.order("userB", 20000);

		// ThreadA: 사용자 A의 주문 금액 조회
		int price = statefulService1.getPrice();
		System.out.println("price = " + price); // 10000원을 기대했지만 20000원이 나옴

		// assertThat(statefulService1.getPrice()).isEqualTo(10000);
		assertThat(statefulService1.getPrice()).isEqualTo(20000);
	}

	static class TestConfig {

		@Bean
		public StatefulService statefulService() {
			return new StatefulService();
		}
	}

}
