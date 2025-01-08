package toy.order.domain.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
public class Item {

    private Long itemId;

    private String itemName;

    private Integer price;

    private Integer quantity;

    private Long memberId;

    public Item(){}

    public Item(String itemName, Integer price, Long memberId, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.memberId = memberId;
        this.quantity = quantity;
    }
}
