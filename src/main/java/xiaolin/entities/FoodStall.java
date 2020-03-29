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
    private Long foodStallId;

    @Column(name = "foodStallName", nullable = false)
    private String foodStallName;

    @Column(name = "foodDescription", length = 5000)
    private String foodStallDescription;

    @Column(name = "rating")
    private float foodStallRating;

    @Column
    private boolean isActive;

//    @OneToMany(targetEntity = User.class, mappedBy = "foodStall", fetch = FetchType.LAZY)
//    private List<User> foodStallOwners = new ArrayList<>();

//    @OneToMany(targetEntity = food.class, mappedBy = "foodStall", fetch = FetchType.EAGER)
//    private List<food> foods = new ArrayList<>();

    @Column(name = "foodStallImage", columnDefinition = "VARCHAR", length = Integer.MAX_VALUE)
    private String foodStallImage;
}
