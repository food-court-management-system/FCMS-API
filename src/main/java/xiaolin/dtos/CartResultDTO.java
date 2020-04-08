package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.CartItem;
import xiaolin.entities.Status;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResultDTO {

    private long id;
    private float totalPrice;
    private Status cartStatus;
    private List<CartItemRes> cartItems = new ArrayList<>();
}
