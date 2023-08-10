package hello.aop.proxyvs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyCastingTest {

	@Test
	@DisplayName("JDK 동적 프록시: 구체 클래스 캐스팅 실패")
	void jdkProxy() {
		MemberServiceImpl target = new MemberServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시

		// 프록시를 인터페이스로 캐스팅 성공
		MemberService memberServiceProxy = (MemberService)proxyFactory.getProxy();
		log.info("proxy class={}", memberServiceProxy.getClass());

		// JDK 동적 프록시를 구체 클래스로 캐스팅을 시도하면 실패: ClassCastException
		assertThrows(ClassCastException.class, () -> {
			MemberServiceImpl memberServiceImplProxy = (MemberServiceImpl)proxyFactory.getProxy();
		});
	}

	@Test
	@DisplayName("CGLIB 프록시: 구체 클래스 캐스팅 성공")
	void cglibProxy() {
		MemberServiceImpl target = new MemberServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.setProxyTargetClass(true); // CGLIB 프록시

		// 프록시를 인터페이스로 캐스팅 성공
		MemberService memberServiceProxy = (MemberService)proxyFactory.getProxy();
		log.info("proxy class={}", memberServiceProxy.getClass());

		// CGLIB 프록시를 구체 클래스로 캐스팅을 시도하면 성공
		MemberServiceImpl memberServiceImplProxy = (MemberServiceImpl)proxyFactory.getProxy();
		assertThat((MemberServiceImpl)proxyFactory.getProxy()).isInstanceOf(MemberServiceImpl.class);
	}

}
