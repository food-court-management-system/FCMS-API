package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Cart;
import xiaolin.entities.Customer;

import java.util.List;

@Service
public interface ICartService {

    List<Cart> getHistoryOrder(Long id);
}
