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
    private int foodStallRating;
    private List<User> foodStallOwner;
    private List<Food> foods;
    private FoodCourtInformation foodCourt;
    private Type foodStallType;
    private String foodStallImage;

    public FoodStallDto() {}

    public FoodStall mapToFoodStall(FoodStallDto foodStallDto) {
        FoodStall result = new FoodStall();
        if (foodStallDto.getFoodStallId() != null) {
            result.setFoodStallId(foodStallDto.getFoodStallId());
        }
        if (foodStallDto.getFoodStallName() != null) {
            result.setFoodStallName(foodStallDto.getFoodStallName());
        }
        if (foodStallDto.getFoodStallDescrption() != null) {
            result.setFoodStallDescription(foodStallDto.getFoodStallDescrption());
        }
        result.setFoodStallRating(foodStallDto.getFoodStallRating());
        if (foodStallDto.getFoodStallOwner() != null) {
            result.setFoodStallOwners(foodStallDto.getFoodStallOwner());
        } else {
            result.setFoodStallOwners(new ArrayList<>());
        }
        if (foodStallDto.getFoods() != null) {
            result.setFoods(foodStallDto.getFoods());
        } else {
            result.setFoods(new ArrayList<>());
        }
        if (foodStallDto.getFoodStallType() != null) {
            result.setFoodStallType(foodStallDto.getFoodStallType());
        }
        if (foodStallDto.getFoodStallImage() != null) {
            result.setFoodStallImage(foodStallDto.getFoodStallImage());
        }
        return result;
    }
}
