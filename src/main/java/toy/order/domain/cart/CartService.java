package toy.order.domain.cart;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartService {

    Optional<Cart> findByItemIdAndMemberId(Long itemId, Long memberId);

    Cart create(Cart cart);

    Integer findItemQuantityByCartId(Long cartId);

    void add(Integer quantity, Long itemId, Long memberId);

    void delete(Long cartId);

    void update(Integer quantity, Long itemId, Long memberId);

    List<Cart> findByMemberId(Long memberId);

    Optional<Cart> findByItemId(Long itemId);
}
