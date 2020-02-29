package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tblFoodStalls")
@Getter
@Setter
public class FoodStall implements Serializable {

    @Id
    @GeneratedValue
    private long foodStallId;

    @Column(name = "foodStallName", nullable = false)
    private String foodStallName;

    @Column(name = "foodDescription", length = 5000)
    private String foodStallDescription;

    @Column(name = "rating")
    private float foodStallRating;

    @OneToMany(targetEntity = User.class, mappedBy = "foodStall", fetch = FetchType.LAZY)
    private List<User> foodStallOwners = new ArrayList<>();

    @OneToMany(targetEntity = Food.class, mappedBy = "foodStall", fetch = FetchType.EAGER)
    private List<Food> foods = new ArrayList<>();

    @ManyToOne(targetEntity = FoodCourtInformation.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "food_court_id")
    private FoodCourtInformation foodCourt;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "type_id")
    private Type foodStallType;

    @Column(name = "foodStallImage", columnDefinition = "VARCHAR", length = Integer.MAX_VALUE)
    private String foodStallImage;
}
