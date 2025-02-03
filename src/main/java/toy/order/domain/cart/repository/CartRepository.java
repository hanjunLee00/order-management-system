package toy.order.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toy.order.domain.cart.Cart;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMember_MemberId(Long memberId);
    Cart findByItem_ItemId(Long itemId);
    Optional<Cart> findByItem_ItemIdAndMember_MemberId(Long itemId, Long memberId);


    @Query("SELECT i.quantity FROM Cart c JOIN c.item i WHERE c.cartId = :cartId")
    Integer findItemQuantityByCartId(@Param("cartId") Long cartId);


    @Modifying
    @Query("UPDATE Cart c SET c.quantity = c.quantity + :quantity WHERE c.item.itemId = :itemId AND c.member.memberId = :memberId")
    void add(@Param("quantity") Integer quantity, @Param("itemId") Long itemId, @Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE Cart c SET c.quantity = :quantity WHERE c.item.itemId =:itemId AND c.member.memberId =:memberId")
    void update(@Param("quantity") Integer quantity, @Param("itemId") Long itemId, @Param("memberId") Long memberId);
}
