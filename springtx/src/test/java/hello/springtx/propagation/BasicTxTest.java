package hello.springtx.propagation;

import static org.assertj.core.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class BasicTxTest {

	@Autowired
	PlatformTransactionManager txManager;

	@TestConfiguration
	static class BasicTxTestConfig {

		@Bean
		public PlatformTransactionManager transactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}

	}

	@DisplayName("트랜잭션 커밋: 트랜잭션 매니저를 통해 트랜잭션 시작 후 커밋")
	@Test
	void commit() {
		log.info("트랜잭션 시작");
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("트랜잭션 커밋 시작");
		txManager.commit(status);
		log.info("트랜잭션 커밋 완료");
	}

	@DisplayName("트랜잭션 롤백: 트랜잭션 매니저를 통해 트랜잭션 시작 후 롤백")
	@Test
	void rollback() {
		log.info("트랜잭션 시작");
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("트랜잭션 롤백 시작");
		txManager.rollback(status);
		log.info("트랜잭션 롤백 완료");
	}

	@DisplayName("트랜잭션 두개 사용: 모두 커밋")
	@Test
	void double_commit() {
		log.info("트랜잭션1 시작");
		TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("트랜잭션1 커밋 시작");
		txManager.commit(tx1);

		log.info("트랜잭션2 시작");
		TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("트랜잭션2 커밋 시작");
		txManager.commit(tx2);
	}

	@DisplayName("트랜잭션 두개 사용: 커밋, 롤백")
	@Test
	void double_commit_rollback() {
		log.info("트랜잭션1 시작");
		TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("트랜잭션1 커밋 시작");
		txManager.commit(tx1);

		log.info("트랜잭션2 시작");
		TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("트랜잭션2 롤백 시작");
		txManager.rollback(tx2);
	}

	@DisplayName("트랜잭션 전파: 내부, 외부 트랜잭션 커밋")
	@Test
	void inner_commit() {
		log.info("외부 트랜잭션 시작");
		TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

		log.info("내부 트랜잭션 시작");
		TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

		log.info("내부 트랜잭션 커밋");
		txManager.commit(inner);

		log.info("외부 트랜잭션 커밋");
		txManager.commit(outer);
	}

	@DisplayName("외부 트랜잭션 롤백: 물리 트랜잭션 롤백")
	@Test
	void outer_rollback() {
		log.info("외부 트랜잭션 시작");
		TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("내부 트랜잭션 시작");
		TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("내부 트랜잭션 커밋");
		txManager.commit(inner);

		log.info("외부 트랜잭션 롤백");
		txManager.rollback(outer);
	}

	@DisplayName("내부 트랜잭션 롤백: 물리 트랜잭션 롤백")
	@Test
	void inner_rollback() {
		log.info("외부 트랜잭션 시작");
		TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("내부 트랜잭션 시작");
		TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());

		log.info("내부 트랜잭션 롤백: rollbackOnly = true");
		txManager.rollback(inner);

		log.info("외부 트랜잭션 커밋: UnexpectedRollbackException");
		log.info("inner rollbackOnly={}", inner.isRollbackOnly());
		log.info("outer rollbackOnly={}", outer.isRollbackOnly());
		assertThat(inner.isRollbackOnly()).isTrue();
		assertThat(outer.isRollbackOnly()).isTrue();
		assertThatThrownBy(() -> txManager.commit(outer))
			.isInstanceOf(UnexpectedRollbackException.class);
	}

}
