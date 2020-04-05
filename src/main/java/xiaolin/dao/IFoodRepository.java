package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Food;

import java.util.List;

@Repository
public interface IFoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByIdIn(List<Long> ids);

    @Query(value = "SELECT f.* FROM tbl_foods f WHERE f.id = :id AND f.is_active = 'TRUE'", nativeQuery = true)
    Food getFoodDetailById(@Param("id") Long foodId);

    @Query(value = "SELECT TOP 10 f.* " +
            "FROM tbl_foods f " +
            "WHERE f.is_active = 'TRUE' " +
            "ORDER BY f.food_rating DESC", nativeQuery = true)
    List<Food> getAllTopFoodInFoodCourt();

    @Query(value = "SELECT f.* FROM tbl_foods f WHERE f.food_stall_id =:id AND f.is_active = 'TRUE'", nativeQuery = true)
    List<Food> getFoodStallMenu(@Param("id") Long foodStallId);

    List<Food> findAllByFoodStall_FoodStallIdAndIsActive(Long foodStallId, boolean isActive);
}
