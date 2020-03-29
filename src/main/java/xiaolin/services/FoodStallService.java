package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xiaolin.dao.IFoodStallRepository;
import xiaolin.dtos.FoodStallDto;
import xiaolin.dtos.food.FoodStallInfoDTO;
import xiaolin.entities.FoodStall;

import java.io.File;
import java.util.ArrayList;
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
    public List<FoodStall> getTopFoodStallOfFoodCourt() {
        List<FoodStall> topFoodStallList = foodStallRepository.getTopFoodStallOfFoodCourt();
        return topFoodStallList;
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
    public List<FoodStall> filterFoodStallByRating() {
        return foodStallRepository.filterFoodStallByRating();
    }

    @Override
    public List<FoodStall> filterFoodStallByCategory(String category) {
        return foodStallRepository.filterFoodStallByCategory(category);
    }

    @Override
    public FoodStall editFoodStall(FoodStall foodStall) {
        if (foodStall.getFoodStallId() == null) {
            return null;
        } else {
            String description = foodStall.getFoodStallDescription();
            String name = foodStall.getFoodStallName();
            String image = foodStall.getFoodStallImage();
            Long id = foodStall.getFoodStallId();
            return foodStallRepository.editDescription(description, name, image, id);
        }
    }
}
