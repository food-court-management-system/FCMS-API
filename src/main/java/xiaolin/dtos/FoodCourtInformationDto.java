package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.FoodCourtInformation;

@Setter
@Getter
public class FoodCourtInformationDto {

    private Long foodCourtId;
    private String foodCourtName;
    private String foodCourtDescription;
    private String address;
    private String foodCourtImage;

    public FoodCourtInformationDto() {
    }

    public FoodCourtInformation mapToFoodCourtInformation(FoodCourtInformationDto foodCourtInformationDto){
        FoodCourtInformation result = new FoodCourtInformation();
        if (foodCourtInformationDto.getFoodCourtId() != 0){
            result.setFoodCourtId(foodCourtInformationDto.getFoodCourtId());
        }
        if (foodCourtInformationDto.getFoodCourtName() != null) {
            result.setFoodCourtName(foodCourtInformationDto.getFoodCourtName());
        }
        if (foodCourtInformationDto.getFoodCourtDescription() != null) {
            result.setFoodCourtDescription(foodCourtInformationDto.getFoodCourtDescription());
        }
        if (foodCourtInformationDto.getAddress() != null) {
            result.setFoodCourtAddress(foodCourtInformationDto.getAddress());
        }
        if (foodCourtInformationDto.getFoodCourtImage() != null) {
            result.setFoodCourtImage(foodCourtInformationDto.getFoodCourtImage());
        }
        return result;
    }
}
