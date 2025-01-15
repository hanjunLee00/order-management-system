package toy.order.domain.item;

import toy.order.domain.item.dto.ItemUpdateDto;
import toy.order.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save (Item item);

    void update(Long itemId, ItemUpdateDto form);

    void deductCnt(Item item, Integer quantity);

    void delete(Long itemId);

    Optional<Item> findByItemId(Long itemId);

    Long findItemIdByItemNameAndMember(String itemName, Member member);

    Member findMemberByItemId(Long itemId);

    String findItemNameByItemId(Long itemId);

    List<Item> findAll();

    List<Item> findItems(ItemSearchCond cond);

    Integer findPriceByItemId(Long itemId);
}
