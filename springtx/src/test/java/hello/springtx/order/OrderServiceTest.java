package hello.springtx.order;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class OrderServiceTest {

	@Autowired
	OrderService service;
	@Autowired
	OrderRepository orderRepository;

	@DisplayName("주문 정상 완료: 트랜잭션 커밋, payStatus는 완료")
	@Test
	void complete() throws NotEnoughMoneyException {
		// given
		Order order = new Order();
		order.setUsername("정상");

		// when
		service.order(order);

		// then
		Order findOrder = orderRepository.findById(order.getId()).get();
		assertThat(findOrder.getPayStatus()).isEqualTo("완료");
	}

	@DisplayName("주문 시스템 에러 발생: 트랜잭션 롤백")
	@Test
	void runtimeException() throws RuntimeException {
		// given
		Order order = new Order();
		order.setUsername("예외");

		// when & then
		assertThatThrownBy(() -> service.order(order))
			.isInstanceOf(RuntimeException.class);
		Optional<Order> orderOptional = orderRepository.findById(order.getId());
		assertThat(orderOptional.isEmpty()).isTrue();
	}

	@DisplayName("비즈니스 예외 발생: 트랜잭션 커밋, payStatus는 대기")
	@Test
	void bizException() {
		// given
		Order order = new Order();
		order.setUsername("잔고부족");

		// when
		try {
			service.order(order);
		} catch (NotEnoughMoneyException e) {
			log.info("고객에게 잔고 부족을 알리고 별도 계좌로 입금하도록 안내");
		}

		// then
		Order findOrder = orderRepository.findById(order.getId()).get();
		assertThat(findOrder.getPayStatus()).isEqualTo("대기");
	}

}
