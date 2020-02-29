package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Cart;
import xiaolin.entities.Customer;
import xiaolin.entities.Rating;
import xiaolin.entities.Wallet;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CustomerDto {

    private Long id;
    private Wallet wallet;
    private String email;
    private boolean isActive;
    private String provider;
    private List<Cart> shoppingCart;
    private List<Rating> ratings;

    public CustomerDto() {
    }

}
