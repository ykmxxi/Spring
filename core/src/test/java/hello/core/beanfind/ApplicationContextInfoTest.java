package hello.core.beanfind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;

public class ApplicationContextInfoTest {

	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

	@DisplayName("모든 빈 출력하기")
	@Test
	void findAllBean() {
		String[] beanDefinitionNames = ac.getBeanDefinitionNames();

		for (String beanDefinitionName : beanDefinitionNames) {
			Object bean = ac.getBean(beanDefinitionName);
			System.out.println("name = " + beanDefinitionName + ", object = " + bean);
		}
	}

	@DisplayName("애플리케이션 빈 출력하기")
	@Test
	void findApplicationBean() {
		String[] beanDefinitionNames = ac.getBeanDefinitionNames();

		for (String beanDefinitionName : beanDefinitionNames) {
			BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

			// 사용자가 정의한 빈: BeanDefinition.ROLE_APPLICATION
			if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
				Object bean = ac.getBean(beanDefinitionName);
				System.out.println("name = " + beanDefinitionName + ", object = " + bean);
			}
		}
	}

	@DisplayName("스프링내부 빈 출력하기")
	@Test
	void findInfrastructureBean() {
		String[] beanDefinitionNames = ac.getBeanDefinitionNames();

		for (String beanDefinitionName : beanDefinitionNames) {
			BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

			// 스프링 내부 빈: BeanDefinition.ROLE_APPLICATION
			if (beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) {
				Object bean = ac.getBean(beanDefinitionName);
				System.out.println("name = " + beanDefinitionName + ", object = " + bean);
			}
		}
	}

}
