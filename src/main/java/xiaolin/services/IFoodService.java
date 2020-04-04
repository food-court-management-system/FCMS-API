package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Food;

import java.util.List;

@Service
public interface IFoodService {

    Food saveFood(Food food);

    List<Food> getAllFood();

    List<Food> getTopFoodOfFoodCourt();

    Food getFoodDetailById(Long foodId);

    List<Food> getFoodStallMenu(Long foodStallId);
}
