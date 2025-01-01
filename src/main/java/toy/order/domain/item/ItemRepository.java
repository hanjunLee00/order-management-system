package toy.order.domain.item;

import java.util.List;

public interface ItemRepository {

    Item save (Item item);

    void update(Long itemId, Item itemParam);

    void delete(Long itemId);

    Item findByItemId(Long itemId);

    Long findItemIdByItemNameAndMemberId(String itemName, Long memberId);

    Long findMemberIdByItemId(Long itemId);

    List<Item> findAll();
}
