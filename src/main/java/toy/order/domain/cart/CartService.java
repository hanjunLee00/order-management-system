package toy.order.domain.cart;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartService {

    Cart add(Cart cart);

    void delete(Cart cart);

    List<Cart> findByMemberId(Long memberId);

    Optional<Cart> findByItemId(Long itemId);
}
