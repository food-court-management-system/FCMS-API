package xiaolin.dtos.foodcourt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FoodCourtDetailDTO implements Serializable {

    private String foodCourtName;
    private String foodCourtDescription;
    private String foodCourtAddress;
}
