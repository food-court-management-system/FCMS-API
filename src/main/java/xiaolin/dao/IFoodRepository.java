package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Food;

import java.util.List;

@Repository
public interface IFoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByIdIn(List<Long> ids);

//    food updateFood(@Param("name") String name,
//                    @Param("description") String description,
//                    @Param("image") String image,
//                    @Param("price") float originPrice,
//                    @Param("retail") float retailPrice
//                    );
}
