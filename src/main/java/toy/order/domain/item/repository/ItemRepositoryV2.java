package toy.order.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.order.domain.item.Item;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}
