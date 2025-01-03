package toy.order.domain.item.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemUpdateForm {
    @NotNull
    private Long itemId;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 0, max = 1000000)
    private Double price;

    @NotNull
    private Integer quantity;
}
