package toy.order.domain.member.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChargeForm {

    @NotNull(message = "충전 금액을 입력하세요")
    @Min(value = 1000, message = "1000원 이상의 금액을 입력하세요")
    private Integer amount;
}
