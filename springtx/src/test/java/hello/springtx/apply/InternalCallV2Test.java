package hello.springtx.apply;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class InternalCallV2Test {

	@Autowired
	CallService callService;

	@DisplayName("callService 프록시 확인")
	@Test
	void printProxy() {
		log.info("callService class={}", callService.getClass());
	}

	@DisplayName("클래스 분리로 프록시 내부 호출 문제 해결")
	@Test
	void externalCall() {
		callService.external();
	}

	@TestConfiguration
	static class InternalCallV2TestConfig {

		@Bean
		CallService callService() {
			return new CallService(internalService());
		}

		@Bean
		InternalService internalService() {
			return new InternalService();
		}

	}

	@Slf4j
	@RequiredArgsConstructor
	static class CallService {

		private final InternalService internalService;

		public void external() {
			log.info("call external");
			printTxInfo();
			internalService.internal();
		}

		private void printTxInfo() {
			boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
			log.info("tx active={}", txActive);
		}

	}

	static class InternalService {

		@Transactional
		public void internal() {
			log.info("call internal");
			printTxInfo();
		}

		private void printTxInfo() {
			boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
			log.info("tx active={}", txActive);
		}

	}

}
