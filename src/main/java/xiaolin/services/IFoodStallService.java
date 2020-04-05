package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.FoodStall;

import java.util.List;

@Service
public interface IFoodStallService {

    List<FoodStall> listAllActiveFoodStall();

    FoodStall saveFoodStallToDB(FoodStall foodStall);

    FoodStall getFoodStallDetail(Long id);

    List<FoodStall> getTopFoodStallOfFoodCourt();
    //Lấy all stall, lấy top food, lấy top stall, lấy food theo stall

    List<FoodStall> searchFoodStallByName(String name);

    List<FoodStall> filterFoodStallByCategory(String category);

    List<FoodStall> searchFoodStallBaseOnFoodName(String foodName);
}
