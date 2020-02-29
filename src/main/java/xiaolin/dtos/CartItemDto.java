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

    public CartItemDto() {}

    public CartItem mapToCartItem(CartItemDto cartItemDto) {
        CartItem result = new CartItem();
        if (cartItemDto.getId() != null) {
            result.setId(cartItemDto.getId());
        }
        if (cartItemDto.getFoodId() != null) {
            result.setFoodId(cartItemDto.getFoodId());
        }
        result.setQuantity(cartItemDto.getQuantity());
        if (cartItemDto.getNote() != null) {
            result.setNote(cartItemDto.getNote());
        }
        if (cartItemDto.getFoodStatus() != null) {
            result.setFoodStatus(cartItemDto.getFoodStatus());
        }
        if (cartItemDto.getCartOwner() != null) {
            result.setCartOwner(cartItemDto.getCartOwner());
        }
        return result;
    }
}
