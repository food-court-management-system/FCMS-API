package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xiaolin.entities.CartItem;

import java.util.List;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart_Id(Long cartId);
}
