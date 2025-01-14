package toy.order.domain.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemPurchaseDto {

    @NotNull
    @Range(min = 1)
    private Integer quantity;
}
