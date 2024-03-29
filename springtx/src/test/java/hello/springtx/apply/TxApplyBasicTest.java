package hello.springtx.apply;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class TxApplyBasicTest {

	@Autowired
	BasicService basicService;

	@DisplayName("프록시 적용 테스트")
	@Test
	void proxyCheck() {
		log.info("aop class={}", basicService.getClass());
		assertThat(AopUtils.isAopProxy(basicService)).isTrue();
	}

	@DisplayName("트랜잭션 적용, 비적용 확인")
	@Test
	void txTest() {
		basicService.tx();
		basicService.nonTx();
	}

	@TestConfiguration
	static class TxApplyBasicConfig {

		@Bean
		BasicService basicService() {
			return new BasicService();
		}

	}

	@Slf4j
	static class BasicService {

		@Transactional
		public void tx() {
			log.info("call tx");
			boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
			log.info("tx active={}", txActive);
		}

		public void nonTx() {
			log.info("call nonTx");
			boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
			log.info("tx active={}", txActive);

		}

	}

}
