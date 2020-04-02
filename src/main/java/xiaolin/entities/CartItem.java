package xiaolin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblCartItems")
@Getter
@Setter
public class CartItem implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "food_id")
    @ManyToOne
    @JsonIgnore
    private Food foodId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @Enumerated(EnumType.STRING)
    private FoodStatus foodStatus;

    @Column(name = "purchased_price")
    private float purchasedPrice;

    @JoinColumn(name = "cart_id")
    @ManyToOne
    @JsonIgnore
    private Cart cart;
}
