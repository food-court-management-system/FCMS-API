package xiaolin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Status cartStatus;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @OneToMany(cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;
}
