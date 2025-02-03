package toy.order.domain.item.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import toy.order.domain.item.Item;
import toy.order.domain.item.dto.ItemSearchCond;

import java.util.List;
import static toy.order.domain.item.QItem.item;

//복잡한 쿼리만 처리하는 Querydsl repository
@Repository
public class ItemQueryRepositoryV2 {

    private final JPAQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond){
        return query.select(item)
                .from(item)
                .where(
                        likeItemName(cond.getItemName()),
                        maxPrice(cond.getMaxPrice())
                )
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName) {
        if(StringUtils.hasText(itemName)){
            return item.itemName.like('%' + itemName + '%');
        }
        return null;
    }

    private Predicate maxPrice(Integer maxPrice) {
        if(maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }

}
