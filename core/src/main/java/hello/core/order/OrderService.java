package hello.core.order;

public interface OrderService {

	/**
	 *
	 * @param memberId, 회원 ID
	 * @param itemName, 상품명
	 * @param itemPrice, 상품 원가
	 * @return 주문
	 */
	Order createOrder(Long memberId, String itemName, int itemPrice);

}
