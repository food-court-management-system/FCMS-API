package xiaolin.dtos.food;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
public class FoodStallInfoDTO implements Serializable {

    private Long foodStallId;
    private String foodStallName;
    private MultipartFile foodStallImage;
    private float foodStallRating;
}
