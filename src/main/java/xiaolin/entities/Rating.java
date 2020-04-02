package xiaolin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

@Entity
@Table(name = "tblRating")
@Getter
@Setter
public class Rating implements Serializable {

    @EmbeddedId
    RatingKey id;

    @ManyToOne
    @MapsId("customer_id")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @MapsId("food_id")
    @JoinColumn(name = "food_id")
    private Food food;

    @Column(name = "rating_star")
    private float ratingStar;

    @Column(name = "rating_date")
    private LocalDate ratingDate;
}
