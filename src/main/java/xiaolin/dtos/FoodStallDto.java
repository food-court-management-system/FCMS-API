package xiaolin.dtos;


import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FoodStallDto {

    private Long foodStallId;
    private String foodStallName;
    private String foodStallDescrption;
    private float foodStallRating;
    private String foodStallImage;

    public FoodStallDto() {}

}
