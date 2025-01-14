package toy.order.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberUpdateDto {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;
}
