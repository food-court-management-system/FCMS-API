package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
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
        FoodCourtInformation result = foodCourtRepository.getFoodCourtInformation();
        FoodCourtInformationDto dto = null;
        if (result != null) {
            dto = new FoodCourtInformationDto();
            dto.setAddress(result.getFoodCourtAddress());
            dto.setFoodCourtDescription(result.getFoodCourtDescription());
            dto.setFoodCourtImage(result.getFoodCourtImage());
            dto.setFoodCourtName(result.getFoodCourtName());
        }
        return FCMSMapper.mapToFoodCourtInformation(dto);
    }
}
