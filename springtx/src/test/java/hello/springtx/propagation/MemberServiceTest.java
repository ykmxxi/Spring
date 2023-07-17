package hello.springtx.propagation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	LogRepository logRepository;

	/**
	 * memberService 	@Transactional: OFF
	 * memberRepository	@Transactional: ON
	 * logRepository	@Transactional: ON
	 */
	@DisplayName("서비스 계층에 트랜잭션이 없을 때: 두 리포지토리 모두 커밋 상황")
	@Test
	void outerTxOff_success() {
		// given
		String username = "outerTxOff_success";

		// when
		memberService.joinV1(username);

		// then: 모든 데이터가 정상 저장
		// junit.jupiter 라이브러리 사용
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

	/**
	 * memberService 	@Transactional: OFF
	 * memberRepository	@Transactional: ON
	 * logRepository	@Transactional: ON, RuntimeException
	 */
	@DisplayName("서비스 계층에 트랜잭션이 없을 때: logRepository 예외 발생, 롤백")
	@Test
	void outerTxOff_fail() {
		// given
		String username = "로그예외_outerTxOff_fail";

		// when
		assertThatThrownBy(() -> memberService.joinV1(username))
			.isInstanceOf(RuntimeException.class);

		// then: 완전히 롤백되지 않고 member 데이터가 DB에 저장(트랜잭션이 분리되어 있음) -> 데이터 정합성 문제 발생
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isEmpty());
	}

	/**
	 * memberService 	@Transactional: ON
	 * memberRepository	@Transactional: OFF
	 * logRepository	@Transactional: OFF
	 */
	@DisplayName("서비스 계층 트랜잭션 적용: 단일 트랜잭션")
	@Test
	void singleTx() {
		// given
		String username = "singleTx";

		// when
		memberService.joinV1(username);

		// then: 모든 데이터가 정상 저장
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

	/**
	 * memberService 	@Transactional: ON
	 * memberRepository	@Transactional: ON
	 * logRepository	@Transactional: ON
	 */
	@DisplayName("서비스 계층 트랜잭션 적용: 단일 트랜잭션")
	@Test
	void outerTxOn_success() {
		// given
		String username = "outerTxOn_success";

		// when
		memberService.joinV1(username);

		// then: 모든 데이터가 정상 저장
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

}
