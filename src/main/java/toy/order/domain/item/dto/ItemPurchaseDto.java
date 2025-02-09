package toy.order.domain.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import toy.order.domain.item.Item;

@Data
public class ItemPurchaseDto {

    @NotNull(message = "개수 입력은 필수입니다")
    @Range(min = 1)
    private Integer quantity;
}
