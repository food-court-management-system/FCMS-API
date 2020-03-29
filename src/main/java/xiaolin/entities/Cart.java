package xiaolin.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private float totalPrice;

    @Column(name = "is_check_out")
    private boolean isCheckOut;

    @Column(name = "cart_status", columnDefinition = "VARCHAR(50)")
    private Enum<Status> cartStatus;

    public enum Status {AVAILABLE, INPROGRESS, PENDING, DONE, FREEZE}

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "customer_id")
    private Customer customerOwner;
}
