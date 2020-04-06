package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FoodCourtDetailInfoDTO implements Serializable {
    private String foodCourtName;
    private String foodCourtDescription;
    private String foodCourtAddress;
}
