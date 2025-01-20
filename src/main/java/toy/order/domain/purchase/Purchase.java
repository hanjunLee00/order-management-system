package toy.order.domain.purchase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import toy.order.domain.item.Item;
import toy.order.domain.member.Member;

@Data
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    //기본 생성자: JPA 사용
    public Purchase() {
    }

    public Purchase(Integer quantity, Member member, Item item) {
        this.quantity = quantity;
        this.member = member;
        this.item = item;
    }
}
