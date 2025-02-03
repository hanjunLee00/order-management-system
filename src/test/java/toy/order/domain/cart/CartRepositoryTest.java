package toy.order.domain.cart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import toy.order.domain.cart.repository.CartRepository;
import toy.order.domain.cart.service.CartService;
import toy.order.domain.item.repository.ItemRepository;

@SpringBootTest
class CartRepositoryTest {

    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void findByMember_MemberId() {
    }

    @Test
    void findByItem_ItemId() {
    }

    @Test
    void findByItem_ItemIdAndMember_MemberId() {
    }

    @Test
    void findItem_ItemQuantityByCartId() {
        //given
        //when

        //then
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }
}