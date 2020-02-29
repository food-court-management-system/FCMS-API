package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Cart;
import xiaolin.entities.Customer;

import java.util.Calendar;

@Setter
@Getter
public class CartDto {

    private Long id;
    private double totalPrice;
    private boolean isCheckOut;
    private Enum<Cart.Status> cartStatus;
    private Calendar purchaseDate;
    private Customer customerOwner;

    public CartDto() { }

    public Cart mapToCart(CartDto cartDto) {
        Cart result = new Cart();
        if (cartDto.getId() != null) {
            result.setId(cartDto.getId());
        }
        result.setTotalPrice(cartDto.getTotalPrice());
        result.setCheckOut(cartDto.isCheckOut());
        if (cartDto.getCartStatus() != null) {
            result.setCartStatus(cartDto.getCartStatus());
        }
        if (cartDto.getPurchaseDate() != null) {
            result.setPurchaseDate(cartDto.getPurchaseDate());
        }
        if (cartDto.getCustomerOwner() != null) {
            result.setCustomerOwner(cartDto.getCustomerOwner());
        }
        return result;
    }
}
