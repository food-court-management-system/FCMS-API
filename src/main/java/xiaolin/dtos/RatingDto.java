package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Customer;
import xiaolin.entities.Food;

import java.time.LocalDate;

@Getter
@Setter
public class RatingDto {

    private Customer customerRating;
    private Food foodRated;
    private float ratingStar;
    private LocalDate ratingDate;

    public RatingDto() { }
}
