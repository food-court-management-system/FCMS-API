package xiaolin.entities;

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

    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @Column(name = "food_status", columnDefinition = "VARCHAR(50)")
    private Enum<FoodStatus> foodStatus;

    @ManyToOne(targetEntity = Cart.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "cart_id")
    private Cart cartOwner;

    public enum FoodStatus{DONE, INPROGRESS, QUEUE, DELIVERY, FINISH}
}
