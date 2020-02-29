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

}
