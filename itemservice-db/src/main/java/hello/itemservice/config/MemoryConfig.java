package hello.itemservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.memory.MemoryItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;

@Configuration
public class MemoryConfig {

	@Bean
	public ItemService itemService() {
		return new ItemServiceV1(itemRepository());
	}

	@Bean
	public ItemRepository itemRepository() {
		return new MemoryItemRepository();
	}

}
