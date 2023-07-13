package hello.springtx.order;

/**
 * 주문 시 결제 잔고가 부족할 때 발생하는 비즈니스 예외
 * Checked Exception
 */
public class NotEnoughMoneyException extends Exception {

	public NotEnoughMoneyException(String message) {
		super(message);
	}

}
