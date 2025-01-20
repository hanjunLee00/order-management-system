package toy.order.domain.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceV1 implements CartService {

    private final CartRepository cartRepository;

    @Override
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }

    @Override
    public Cart add(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> findByMemberId(Long memberId) {
        return cartRepository.findByMember_MemberId(memberId);
    }

    @Override
    public Optional<Cart> findByItemId(Long itemId) {
        return Optional.ofNullable(cartRepository.findByItem_ItemId(itemId));
    }
}
