package xiaolin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblWallets")
@Getter
@Setter
public class Wallet implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "balances")
    private float balances;

    @Column(name = "in_use_balances")
    private float inUseBalances;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToOne
    @JsonIgnore
    private Customer customer;
    
}
