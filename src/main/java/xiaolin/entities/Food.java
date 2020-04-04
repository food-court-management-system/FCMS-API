package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @Column(name = "origin_price")
    private float originPrice;

    @Column(name = "retail_price")
    private float retailPrice;

    @Column(name = "foodDescription", length = 1000)
    private String foodDescription;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private Type foodType;

    @ManyToOne(targetEntity = FoodStall.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "food_stall_id")
    private FoodStall foodStall;

    @Column(name = "food_rating")
    private float foodRating;

    @Column(name = "foodImage", columnDefinition = "VARCHAR", length = 1000)
    private String foodImage;

    @Column(name = "is_active")
    private Boolean isActive;

//    @OneToMany(targetEntity = Rating.class, mappedBy = "food")
//    private List<Rating> rating;
}
