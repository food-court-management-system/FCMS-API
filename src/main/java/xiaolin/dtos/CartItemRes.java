package xiaolin.dtos;

import lombok.Data;

@Data
public class CartItemRes {
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
