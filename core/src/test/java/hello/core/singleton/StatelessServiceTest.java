package hello.core.singleton;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatelessServiceTest {

	@Test
	void statelessServiceSingleton() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

		StatelessService statefulService1 = ac.getBean(StatelessService.class);
		StatelessService statefulService2 = ac.getBean(StatelessService.class);

		// ThreadA: A 사용자가 10000원 주문
		int userAPrice = statefulService1.order("userA", 10000);

		// ThreadB: B 사용자가 20000원 주문
		int userBPrice = statefulService2.order("userB", 20000);

		assertThat(userAPrice).isEqualTo(10000);
		assertThat(userBPrice).isEqualTo(20000);
	}

	static class TestConfig {

		@Bean
		public StatelessService statelessService() {
			return new StatelessService();
		}
	}

}
