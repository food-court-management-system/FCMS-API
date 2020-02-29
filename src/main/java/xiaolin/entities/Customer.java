package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tblCustomers")
@Getter
@Setter
public class Customer implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(targetEntity = Wallet.class, fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Wallet wallet;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "provider")
    private String provider;

    @OneToMany(targetEntity = Cart.class, mappedBy = "customerOwner", fetch = FetchType.LAZY)
    private List<Cart> shoppingCart = new ArrayList<>();
}
