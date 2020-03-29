package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Food;

@Repository
public interface IFoodRepository extends JpaRepository<Food, Long> {

//    food updateFood(@Param("name") String name,
//                    @Param("description") String description,
//                    @Param("image") String image,
//                    @Param("price") float originPrice,
//                    @Param("retail") float retailPrice
//                    );
}
