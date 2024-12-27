package toy.order.domain.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;
    private Long memberId;

    public Item(){
    }

    public Item(String itemName, Integer price, Integer quantity, Long memberId) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.memberId = memberId;
    }
}
