package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

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

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "foodStallImage", columnDefinition = "VARCHAR", length = Integer.MAX_VALUE)
    private String foodStallImage;

    public boolean getIsActive() {
        return isActive;
    }
}
