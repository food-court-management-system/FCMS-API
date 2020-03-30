package xiaolin.dtos.food;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class FoodCreateDTO implements Serializable {

    private float originPrice;
    private float retailPrice;
    private String foodDescription;
    private String foodName;
    private String foodType;
}
