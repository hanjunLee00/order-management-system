package toy.order.domain.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemUpdateDto {
    @NotNull
    private Long itemId;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 0, max = 1000000)
    private Integer price;

    @NotNull
    private Integer quantity;
}
