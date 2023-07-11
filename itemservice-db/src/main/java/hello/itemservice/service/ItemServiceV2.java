package hello.itemservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.repository.jpa.SpringDataJpaItemRepository;
import lombok.RequiredArgsConstructor;

/**
 * Spring Data JPA 리포지토리를 직접 이용, 구조의 단순화
 */
@Service
@RequiredArgsConstructor
public class ItemServiceV2 implements ItemService {

	private final SpringDataJpaItemRepository itemRepository;

	@Override
	public Item save(Item item) {
		return itemRepository.save(item);
	}

	@Override
	public void update(Long itemId, ItemUpdateDto updateParam) {
		Item findItem = itemRepository.findById(itemId).orElseThrow();
		findItem.setItemName(updateParam.getItemName());
		findItem.setPrice(updateParam.getPrice());
		findItem.setQuantity(updateParam.getQuantity());
	}

	@Override
	public Optional<Item> findById(Long id) {
		return itemRepository.findById(id);
	}

	@Override
	public List<Item> findItems(ItemSearchCond cond) {
		String itemName = cond.getItemName();
		Integer maxPrice = cond.getMaxPrice();

		if (StringUtils.hasText(itemName) && maxPrice != null) {
			return itemRepository.findItems("%" + itemName + "%", maxPrice);
		} else if (StringUtils.hasText(itemName)) {
			return itemRepository.findByItemNameLike("%" + itemName + "%");
		} else if (maxPrice != null) {
			return itemRepository.findByPriceLessThanEqual(maxPrice);
		} else {
			return itemRepository.findAll();
		}
	}

}
