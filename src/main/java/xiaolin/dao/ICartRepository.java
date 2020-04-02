package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Cart;
import xiaolin.entities.CartItem;

import java.util.List;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

    List<Cart> getAllByWallet_Id(Long walletId);
}
