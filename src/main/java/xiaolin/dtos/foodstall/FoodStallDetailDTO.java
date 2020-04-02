package xiaolin.dtos.foodstall;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FoodStallDetailDTO implements Serializable {

    private Long foodStallId;
    private String foodStallName;
    private String foodStallDescription;
    private Float foodStallRating;
}
