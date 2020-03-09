package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.dtos.FoodStallDto;
import xiaolin.entities.FoodStall;

import java.util.List;

@Service
public interface IFoodStallService {

    List<FoodStall> listAllFoodStall();

    FoodStall getFoodStallDetail(Long id);

    List<FoodStall> searchFoodStallByName(String name);

    List<FoodStall> filterFoodStallByRating();

    List<FoodStall> filterFoodStallByCategory(String category);

    FoodStall editFoodStall(FoodStall foodStall);
}
