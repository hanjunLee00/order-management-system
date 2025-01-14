package toy.order.domain.item;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import toy.order.domain.member.Member;

@Data
@Entity
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemName;

    private Integer price;

    private Integer quantity;

    @ManyToOne // 다대일 관계
    @JoinColumn(name = "member_id") // 외래 키 컬럼명
    private Member member; // 연관된 Member 객체

    //기본 생성자
    public Item(){}

    public Item(String itemName, Integer price, Member member, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.member = member;
        this.quantity = quantity;
    }
}
