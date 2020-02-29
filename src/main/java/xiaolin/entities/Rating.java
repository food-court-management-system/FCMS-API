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

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(targetEntity = Customer.class)
    private Customer customer;

    @Column(name = "rating_star")
    private float ratingStar;

    @Column(name = "rating_date")
    private LocalDate ratingDate;

    @ManyToOne(targetEntity = Food.class)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;
}
