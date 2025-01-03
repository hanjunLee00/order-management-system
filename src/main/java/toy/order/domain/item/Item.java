package toy.order.domain.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
public class Item {

    private Long itemId;

    private String itemName;

    private Double price;

    private Integer quantity;

    private Long memberId;
}
