package hello.springtx.apply;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
class InitTxTest {

	@Autowired
	Hello hello;

	@DisplayName("초기화 코드는 스프링 초기화 시점에 호출: 트랜잭션 적용 X, ApplicationReadyEvent로 해결 가능")
	@Test
	void go() {
	}

	@TestConfiguration
	static class InitTxTestConfig {

		@Bean
		Hello hello() {
			return new Hello();
		}

	}

	@Slf4j
	static class Hello {

		@PostConstruct
		@Transactional
		public void initV1() {
			boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
			log.info("Hello init @PostConstruct tx active={}", isActive);
		}

		@EventListener(value = ApplicationReadyEvent.class)
		@Transactional
		public void initV2() {
			boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
			log.info("Hello init ApplicationReadyEvent tx active={}", isActive);
		}

	}

}
