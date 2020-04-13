package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xiaolin.entities.CartItem;

import java.util.List;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart_Id(Long cartId);

    @Query(value = "SELECT cis.* FROM tbl_cart_items cis WHERE cis.food_status = 'CANCEL' AND cis.food_id IN(:listFoodId)", nativeQuery = true)
    List<CartItem> findCancelCartItemByFoodId(@Param("listFoodId") List<Long> foodId);
}
