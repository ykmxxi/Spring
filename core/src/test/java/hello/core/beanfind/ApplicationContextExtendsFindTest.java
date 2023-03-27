package hello.core.beanfind;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;

public class ApplicationContextExtendsFindTest {

	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

	@DisplayName("부모 타입으로 조회, 자식이 둘 이상 있으면 중복 오류 발생")
	@Test
	void findBeanByParentTypeDuplicate() {
		// NoUniqueBeanDefinitionException 발생
		// DiscountPolicy bean = ac.getBean(DiscountPolicy.class);

		assertThrows(NoUniqueBeanDefinitionException.class,
			() -> ac.getBean(DiscountPolicy.class)
		);
	}

	@DisplayName("부모 타입으로 조회, 자식이 둘 이상 있으면 빈 이름을 지정해 조회")
	@Test
	void findBeanByParentTypeBeanName() {
		DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);

		assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
	}

	@DisplayName("특정 하위 타입으로 조회")
	@Test
	void findBeanBySubType() {
		RateDiscountPolicy rateDiscountPolicy = ac.getBean(RateDiscountPolicy.class);

		assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
	}

	@DisplayName("부모 타입으로 모두 조회")
	@Test
	void findAllBeansByParentType() {
		Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);

		assertThat(beansOfType.size()).isEqualTo(2);

		for (String key : beansOfType.keySet()) {
			System.out.println("key = " + key + ", value = " + beansOfType.get(key));
		}
	}

	@DisplayName("부모 타입으로 모두 조회하기- Object")
	@Test
	void findAllBeanByObjectType() {
		Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);

		for (String key : beansOfType.keySet()) {
			System.out.println("key = " + key + ", value = " + beansOfType.get(key));
		}
	}

	@Configuration
	static class TestConfig {

		@Bean
		public DiscountPolicy rateDiscountPolicy() {
			return new RateDiscountPolicy();
		}

		@Bean
		public DiscountPolicy fixDiscountPolicy() {
			return new FixDiscountPolicy();
		}
	}

}
