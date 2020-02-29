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


}
