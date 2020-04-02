package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Food;
import xiaolin.entities.FoodStall;
import xiaolin.entities.Type;

@Getter
@Setter
public class FoodDto {

    private Long foodId;
    private float originPrice;
    private float retailPrice;
    private String foodDescription;
    private String foodName;
    private String foodType;
    private Long foodStallId;
    private String foodImage;

    public FoodDto() {}

}
