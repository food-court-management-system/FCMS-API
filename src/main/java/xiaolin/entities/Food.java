package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblFoods")
@Getter
@Setter
public class Food implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "food_name", length = 200)
    private String foodName;

    @Column(name = "price")
    private float price;

    @Column(name = "foodDescription", length = 1000)
    private String foodDescription;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private Type foodType;

    @ManyToOne(targetEntity = FoodStall.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "food_stall_id")
    private FoodStall foodStall;

    @Column(name = "foodStallImage", columnDefinition = "VARCHAR", length = 1000)
    private String foodImage;
}
