package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Cart;
import xiaolin.entities.CartItem;

@Getter
@Setter
public class CartItemDto {

    private Long id;
    private Long foodId;
    private int quantity;
    private String note;
    private Enum<CartItem.FoodStatus> foodStatus;
    private Cart cartOwner;
    private float purchasePrice;

    public CartItemDto() {}


}
