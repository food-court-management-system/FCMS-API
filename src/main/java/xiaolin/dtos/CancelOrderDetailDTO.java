package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CancelOrderDetailDTO implements Serializable {

    private Long id;
    private String foodName;
    private String reason;
    private int quantity;
    private float purchasedPrice;
    private String purchasedDate;
}
