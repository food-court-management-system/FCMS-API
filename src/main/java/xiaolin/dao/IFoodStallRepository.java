package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.FoodStall;

import java.util.List;

@Repository
public interface IFoodStallRepository extends JpaRepository<FoodStall, Long> {

    @Query(value = "SELECT fs FROM tbl_food_stalls fs WHERE fs.is_active = 'TRUE'", nativeQuery = true)
    List<FoodStall> listAllActiveFoodStall();

    @Query(value = "SELECT * FROM tbl_food_stalls fs ORDER BY fs.rating DESC", nativeQuery = true)
    List<FoodStall> getTopFoodStallOfFoodCourt();

    @Query(value = "SELECT fs FROM FoodStall fs WHERE fs.foodStallName LIKE %:name% ORDER BY fs.foodStallRating ASC")
    List<FoodStall> searchFoodStallByName(@Param("name") String foodStallName);

    @Query(value = "SELECT fs FROM FoodStall fs ORDER BY fs.foodStallRating ASC")
    List<FoodStall> filterFoodStallByRating();

    @Query(value = "SELECT fs FROM FoodStall fs WHERE fs.foodStallId = :id")
    FoodStall getFoodStallDetail(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT fs.*\n" +
            "FROM tbl_types t LEFT JOIN tbl_foods f\n" +
            "ON t.id = f.type_id \n" +
            "RIGHT JOIN tbl_food_stalls fs \n" +
            "ON f.food_stall_id = fs.food_stall_id\n" +
            "WHERE t.type_name =food AND f.food_stall_id = fs.food_stall_id\n" +
            "ORDER BY fs.rating ASC", nativeQuery = true)
    List<FoodStall> filterFoodStallByCategory(String category);

    @Query(value = "UPDATE FoodStall fs SET fs.foodStallDescription = :description, " +
            "fs.foodStallName = :name, fs.foodStallImage = :image WHERE fs.foodStallId = :id")
    FoodStall editDescription(@Param("description") String description,
                              @Param("name") String name,
                              @Param("image") String image,
                              @Param("id") Long id);
}
