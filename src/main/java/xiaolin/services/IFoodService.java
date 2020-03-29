package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Food;

import java.util.List;

@Service
public interface IFoodService {

    Food addNewFood(Food food);

    void deleteFood(Long foodId);

//    food updateFood(Long foodId);

    List<Food> getAllFood();
}
