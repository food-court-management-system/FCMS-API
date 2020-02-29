package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Cart;
import xiaolin.entities.Customer;
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

    public CustomerDto() {
    }

    public Customer mapToCustomer(CustomerDto customerDto) {
        Customer result = new Customer();
        if (customerDto.getId() != null) {
            result.setId(customerDto.getId());
        }
        if (customerDto.getWallet() != null) {
            result.setWallet(customerDto.getWallet());
        }
        if (customerDto.getEmail() != null) {
            result.setEmail(customerDto.getEmail());
        }
        result.setActive(customerDto.isActive());
        if (customerDto.getProvider() != null) {
            result.setProvider(customerDto.getProvider());
        }
        if (customerDto.getShoppingCart() != null) {
            result.setShoppingCart(customerDto.getShoppingCart());
        } else {
            result.setShoppingCart(new ArrayList<>());
        }
        return result;
    }
}
