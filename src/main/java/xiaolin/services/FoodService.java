package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.IFoodRepository;
import xiaolin.entities.Food;

import java.util.List;

@Service
public class FoodService implements IFoodService{

    @Autowired
    IFoodRepository foodRepository;

    @Override
    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public void deleteFood(Long foodId) {
        foodRepository.deleteById(foodId);
    }

//    public food updateFood(Long foodId) {
//        foodRepository.updateFood(foodId);
//    }

    @Override
    public List<Food> getAllFood() {
        return foodRepository.findAll();
    }
}
