package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Cart;
import xiaolin.entities.Customer;

import java.time.LocalDate;
import java.util.Calendar;

@Setter
@Getter
public class CartDto {

    private Long id;
    private float totalPrice;
    private boolean isCheckOut;
    private Enum<Cart.Status> cartStatus;
    private LocalDate purchaseDate;
//    private Customer customerOwner;

    public CartDto() { }


}
