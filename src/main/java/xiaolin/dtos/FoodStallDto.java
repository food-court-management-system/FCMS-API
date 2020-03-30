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

    public FoodStallDto(Long foodStallId, String foodStallName, String foodStallDescrption, float foodStallRating, String foodStallImage) {
        this.foodStallId = foodStallId;
        this.foodStallName = foodStallName;
        this.foodStallDescrption = foodStallDescrption;
        this.foodStallRating = foodStallRating;
        this.foodStallImage = foodStallImage;
    }
}
