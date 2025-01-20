package toy.order.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMember_MemberId(Long memberId);
    Cart findByItem_ItemId(Long itemId);
}
