package toy.order.domain.item;

import toy.order.domain.item.form.ItemUpdateForm;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save (Item item);

    void update(Long itemId, ItemUpdateForm form);

    void updateCnt(Item item, Integer quantity);

    void delete(Long itemId);

    Optional<Item> findByItemId(Long itemId);

    Long findItemIdByItemNameAndMemberId(String itemName, Long memberId);

    Long findMemberIdByItemId(Long itemId);

    List<Item> findAll();

    List<Item> findItems(ItemSearchCond cond);

    Double findPriceByItemId(Long itemId);
}
