package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FoodStallCreateDTO implements Serializable {

    private String foodStallName;
    private String foodStallDescription;
}
