package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblFoodCourtInformation")
@Getter
@Setter
public class FoodCourtInformation implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "food_court_id")
    private Long foodCourtId;

    @Column(name = "foodStallName")
    private String foodCourtName;

    @Column(name = "foodDescription", length = Integer.MAX_VALUE)
    private String foodCourtDescription;

    @Column(name = "address")
    private String foodCourtAddress;

    @Column(name = "foodStallImage", columnDefinition = "VARCHAR", length = 5000)
    private String foodCourtImage;
}
