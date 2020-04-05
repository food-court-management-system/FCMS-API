package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.dtos.CartDto;
import xiaolin.dtos.CartItemRes;
import xiaolin.entities.Cart;
import xiaolin.entities.CartItem;

import java.util.List;

@Service
public interface ICartService {

    List<Cart> getHistoryOrder(Long id);

    boolean order(CartDto cartDto);

    List<CartItem> getOrderDetail(Long cartId);

    List<CartItemRes> getAllCartItemInProcess(Long foodStallId);

    CartItemRes getCartItem(Long cartItemId);
}
