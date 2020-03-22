package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.ICartRepository;
import xiaolin.entities.Cart;
import xiaolin.entities.Customer;

import java.util.List;

@Service
public class CartService implements ICartService{

    @Autowired
    ICartRepository cartRepository;


    @Override
    public List<Cart> getHistoryOrder(Long id) {
//        cartRepository.findAll(new Customer());
        return cartRepository.getHistoryOrder(id);
    }
}
