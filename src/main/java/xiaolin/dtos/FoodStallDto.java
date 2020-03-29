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
//    private List<User> foodStallOwner;
//    private List<Food> foods;
//    private FoodCourtInformation foodCourt;
    private String foodStallImage;

    public FoodStallDto() {}

}
