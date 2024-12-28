package toy.order.domain.item;

import java.util.List;

public interface ItemRepository {

    Item save (Item item);

    void update(Long itemId, Item itemParam);

    void delete(Long itemId);

    Item findByItemId(Long itemId);

    List<Item> findByUuid(String uuid);

    Long findItemIdByItemNameAndMemberId(String itemName, Long memberId);

    List<Item> findAll();
}
