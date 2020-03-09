package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.entities.FoodCourtInformation;

@Service
public interface IFoodCourtService {

    FoodCourtInformation getFoodCourtInformation();
}
