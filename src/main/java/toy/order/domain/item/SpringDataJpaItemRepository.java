package toy.order.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toy.order.domain.member.Member;

import java.math.BigDecimal;
import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    // itemName과 memberId로 아이템 ID 검색
    Long findItemIdByItemNameAndMember(String itemName, Member member);

    @Query("SELECT i.member FROM Item i WHERE i.itemId = :itemId")
    Member findMemberByItemId(@Param("itemId") Long itemId);

    // 아이템 ID로 price 검색
    Double findPriceByItemId(Long itemId);

    List<Item> findByItemNameLike(String itemName);

    List<Item> findByPriceLessThanEqual(Integer price);

    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);

}
