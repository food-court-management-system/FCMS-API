package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.IFoodStallRepository;
import xiaolin.entities.FoodStall;

import java.util.List;

@Service
public class FoodStallService implements IFoodStallService{

    @Autowired
    IFoodStallRepository foodStallRepository;

    @Override
    public List<FoodStall> listAllActiveFoodStall() {
        return foodStallRepository.listAllActiveFoodStall();
    }

    @Override
    public FoodStall saveFoodStallToDB(FoodStall foodStall) {
        return foodStallRepository.save(foodStall);
    }

    @Override
    public List<FoodStall> getTopFoodStallOfFoodCourt() {
        return foodStallRepository.getTopFoodStallOfFoodCourt();
    }

    @Override
    public FoodStall getFoodStallDetail(Long id) {
        return foodStallRepository.getFoodStallDetail(id);
    }

    @Override
    public List<FoodStall> searchFoodStallByName(String name) {
        return foodStallRepository.searchFoodStallByName(name);
    }

    @Override
    public List<FoodStall> filterFoodStallByCategory(String category) {
        return foodStallRepository.filterFoodStallByCategory(category);
    }
}
