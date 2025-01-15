package toy.order.domain.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import toy.order.domain.item.dto.ItemUpdateDto;
import toy.order.domain.member.Member;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository repository;

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto form) {
        Item findItem = repository.findById(itemId).orElseThrow();
        findItem.setItemName(form.getItemName());
        findItem.setPrice(form.getPrice());
        findItem.setQuantity(form.getQuantity());
        //트랜잭션 커밋 시점에 쿼리 날려 업데이트
    }

    @Override
    public void deductCnt(Item item, Integer quantity) {
        item.setQuantity(item.getQuantity() - quantity);
    }

    @Override
    public void delete(Long itemId) {
        repository.deleteById(itemId);
    }

    @Override
    public Optional<Item> findByItemId(Long itemId) {
        return repository.findById(itemId);
    }

    @Override
    public Long findItemIdByItemNameAndMember(String itemName, Member member) {
        return repository.findItemIdByItemNameAndMember(itemName, member);
    }

    @Override
    public Member findMemberByItemId(Long itemId) {
        return repository.findMemberByItemId(itemId);
    }

    @Override
    public String findItemNameByItemId(Long itemId) {
        return repository.findItemNameByItemId(itemId);
    }

    @Override
    public Integer findPriceByItemId(Long itemId) {
        return repository.findPriceByItemId(itemId);
    }

    @Override
    public List<Item> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond){
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        if(StringUtils.hasText(itemName) && maxPrice != null) {
            return repository.findItems("%" + itemName + "%", maxPrice);
        }else if(StringUtils.hasText(itemName)) {
            return repository.findByItemNameLike("%" + itemName + "%");
        }else if(maxPrice != null) {
            return repository.findByPriceLessThanEqual(maxPrice);
        }else
            return repository.findAll();
    }
}

