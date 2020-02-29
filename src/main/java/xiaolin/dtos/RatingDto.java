package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Customer;
import xiaolin.entities.Food;

import java.time.LocalDate;

@Getter
@Setter
public class RatingDto {

    private Long id;
    private Customer customerRating;
    private float ratingStar;
    private LocalDate ratingDate;
    private Food foodRated;

    public RatingDto() { }
}
