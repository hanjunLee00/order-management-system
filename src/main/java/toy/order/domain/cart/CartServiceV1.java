package toy.order.domain.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceV1 implements CartService {

    private final CartRepository cartRepository;

    @Override
    public void delete(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void add(Integer quantity, Long itemId, Long memberId) {
        cartRepository.add(quantity, itemId, memberId);
    }

    @Override
    public void update(Integer quantity, Long itemId, Long memberId) {
        cartRepository.update(quantity, itemId, memberId);
    }

    @Override
    public Optional<Cart> findByItemIdAndMemberId(Long itemId, Long memberId) {
        return cartRepository.findByItem_ItemIdAndMember_MemberId(itemId, memberId);
    }

    @Override
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Integer findItemQuantityByCartId (Long cartId) {
        return cartRepository.findItemQuantityByCartId(cartId);
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
