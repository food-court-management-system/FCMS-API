package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.dtos.FoodStallDto;
import xiaolin.dtos.food.FoodStallInfoDTO;
import xiaolin.entities.FoodStall;

import java.util.List;

@Service
public interface IFoodStallService {

    List<FoodStall> listAllActiveFoodStall();

    FoodStall getFoodStallDetail(Long id);

    List<FoodStall> getTopFoodStallOfFoodCourt();

    List<FoodStall> searchFoodStallByName(String name);

    List<FoodStall> filterFoodStallByRating();

    List<FoodStall> filterFoodStallByCategory(String category);

    FoodStall editFoodStall(FoodStall foodStall);
}
