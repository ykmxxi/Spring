package hello.core.beanfind;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

public class ApplicationContextBasicFindTest {

	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

	@DisplayName("빈 이름으로 조회")
	@Test
	void findBeanByName() {
		MemberService memberService = ac.getBean("memberService", MemberService.class);

		assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
	}

	@DisplayName("이름 없이 타입으로만 조회")
	@Test
	void findBeanByType() {
		MemberService memberService = ac.getBean(MemberService.class);

		assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
	}

	@DisplayName("구체 타입으로 조회")
	@Test
	void findBeanBySpecificType() {
		MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);

		assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
	}

	@DisplayName("빈 이름으로 조회X")
	@Test
	void findBeanByNameFailed() {

		assertThrows(NoSuchBeanDefinitionException.class,
			() -> ac.getBean("xxxx", MemberService.class));
	}

}
