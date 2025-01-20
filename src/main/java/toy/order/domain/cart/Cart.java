package toy.order.domain.cart;


import jakarta.persistence.*;
import lombok.Data;
import toy.order.domain.item.Item;
import toy.order.domain.member.Member;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue
    private Long cartId;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Cart(){}

    public Cart(Integer quantity, Member member, Item item) {
        this.quantity = quantity;
        this.member = member;
        this.item = item;
    }

}
