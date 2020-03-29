package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xiaolin.dao.IFoodCourtRepository;
import xiaolin.dtos.FoodCourtInformationDto;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.entities.FoodCourtInformation;

@Service
public class FoodCourtService implements IFoodCourtService{

    @Autowired
    IFoodCourtRepository foodCourtRepository;

    @Override
    public FoodCourtInformation getFoodCourtInformation() {
        FoodCourtInformation result = foodCourtRepository.findFirstByOrderByFoodCourtIdDesc();

        if (result != null) {
            FoodCourtInformationDto dto = new FoodCourtInformationDto();
            dto.setAddress(result.getFoodCourtAddress());
            dto.setFoodCourtDescription(result.getFoodCourtDescription());
            dto.setFoodCourtImage(result.getFoodCourtImage());
            dto.setFoodCourtName(result.getFoodCourtName());
            return FCMSMapper.mapToFoodCourtInformation(dto);
        }

        return null;
    }

    @Override
    public FoodCourtInformation saveFoodCourtInformation(FoodCourtInformation foodCourtInformation) {
        return foodCourtRepository.save(foodCourtInformation);
    }
}
