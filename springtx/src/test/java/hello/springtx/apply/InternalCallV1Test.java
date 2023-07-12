package hello.springtx.apply;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class InternalCallV1Test {

	@Autowired
	CallService callService;

	@DisplayName("callService 프록시 확인")
	@Test
	void printProxy() {
		log.info("callService class={}", callService.getClass());
	}

	@Test
	void internalCall() {
		callService.internal();
	}

	@Test
	void externalCall() {
		callService.external();
	}

	@TestConfiguration
	static class InternalCallV1TestConfig {

		@Bean
		CallService callService() {
			return new CallService();
		}

	}

	@Slf4j
	static class CallService {

		public void external() {
			log.info("call external");
			printTxInfo();
			internal();
		}

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
