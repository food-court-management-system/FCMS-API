package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.FoodStall;

import java.util.List;

@Repository
public interface IFoodStallRepository extends JpaRepository<FoodStall, Long> {

    @Query(value = "SELECT fs.* FROM tbl_food_stalls fs WHERE fs.is_active = 'TRUE' ORDER BY fs.food_stall_name ASC", nativeQuery = true)
    List<FoodStall> listAllActiveFoodStall();

    @Query(value = "SELECT * FROM tbl_food_stalls fs " +
            "WHERE fs.is_active = 'TRUE' ORDER BY fs.rating DESC", nativeQuery = true)
    List<FoodStall> getTopFoodStallOfFoodCourt();

    @Query(value = "SELECT fs FROM FoodStall fs WHERE fs.foodStallName LIKE %:name% ORDER BY fs.foodStallRating ASC")
    List<FoodStall> searchFoodStallByName(@Param("name") String foodStallName);

    @Query(value = "SELECT fs FROM FoodStall fs WHERE fs.foodStallId = :id")
    FoodStall getFoodStallDetail(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT fs.*\n" +
            "FROM tbl_types t LEFT JOIN tbl_foods f\n" +
            "ON t.id = f.type_id \n" +
            "RIGHT JOIN tbl_food_stalls fs \n" +
            "ON f.food_stall_id = fs.food_stall_id\n" +
            "WHERE t.type_name =:category AND f.food_stall_id = fs.food_stall_id\n" +
            "AND fs.is_active = 'TRUE'" +
            "ORDER BY fs.rating DESC", nativeQuery = true)
    List<FoodStall> filterFoodStallByCategory(@Param("category") String category);

    @Query(value = "SELECT DISTINCT fs.*\n" +
            "FROM tbl_food_stalls fs LEFT JOIN tbl_foods f\n" +
            "ON fs.food_stall_id = f.food_stall_id\n" +
            "WHERE f.food_name LIKE %:name%\n" +
            "AND f.is_active = 'TRUE' AND fs.is_active = 'TRUE'" +
            "\n ORDER BY fs.rating DESC", nativeQuery = true)
    List<FoodStall> searchFoodBaseByName(@Param("name") String foodName);
}
