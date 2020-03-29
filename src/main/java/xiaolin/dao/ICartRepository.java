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

    @Query(value = "SELECT * FROM tbl_shopping_carts c WHERE c.customer_id = :customerId", nativeQuery = true)
    List<Cart> getHistoryOrder(@Param("customerId") Long id);
}
