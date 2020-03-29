package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.FoodCourtInformation;

@Service
public interface IFoodCourtService {

    FoodCourtInformation getFoodCourtInformation();

    FoodCourtInformation saveFoodCourtInformation(FoodCourtInformation foodCourtInformation);
}
