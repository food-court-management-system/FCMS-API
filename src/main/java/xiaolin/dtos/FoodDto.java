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
    private float price;
    private String foodDescription;
    private Type foodType;
    private FoodStall foodStall;
    private String foodImage;

    public FoodDto() {}

    public Food mapToFood(FoodDto foodDto) {
        Food result = new Food();
        if (foodDto.getFoodId() != null) {
            result.setId(foodDto.getFoodId());
        }
        result.setPrice(foodDto.getPrice());
        if (foodDto.getFoodDescription() != null) {
            result.setFoodDescription(foodDto.getFoodDescription());
        }
        if (foodDto.getFoodType() != null) {
            result.setFoodType(foodDto.getFoodType());
        }
        if (foodDto.getFoodStall() != null) {
            result.setFoodStall(foodDto.getFoodStall());
        }
        if (foodDto.getFoodImage() != null) {
            result.setFoodImage(foodDto.getFoodImage());
        }
        return result;
    }
}
