package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Entity
@Table(name = "tblShoppingCarts")
@Getter
@Setter
public class Cart implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "is_check_out")
    private boolean isCheckOut;

    @Column(name = "cart_status", columnDefinition = "VARCHAR(50)")
    private Enum<Status> cartStatus;

    public enum Status {AVAILABLE, INPROGRESS, PENDING, DONE, FREEZE}

    @Column(name = "purchase_date")
    private Calendar purchaseDate;

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "customer_id")
    private Customer customerOwner;
}
