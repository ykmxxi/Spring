package hello.aop.exam;

import org.springframework.stereotype.Repository;

import hello.aop.exam.annotation.Retry;
import hello.aop.exam.annotation.Trace;

@Repository
public class ExamRepository {

	private static int seq = 0;

	/**
	 * 5번 실행 중 1번 실패하는 요청
	 */
	@Trace
	@Retry(value = 4) // 최대 재시도 횟수 = 4
	public String save(String itemId) {
		seq++;
		if (seq % 5 == 0) {
			throw new IllegalStateException("예외 발생");
		}
		return "ok";
	}

}
