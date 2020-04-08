package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResResult {
    private Long id;
    private Long foodId;
    private String foodName;
    private Long foodStallId;
    private String foodStallName;
    private int quantity;
    private String note;
    private String foodStatus;
    private float purchasedPrice;
}
