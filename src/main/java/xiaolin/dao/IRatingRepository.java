package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Rating;

import java.util.List;

@Repository
public interface IRatingRepository extends JpaRepository<Rating, Long>{

    @Query(value = "SELECT SUM(r.rating_star) as rating_star, r.food_id " +
            "FROM tbl_rating r " +
            "WHERE r.food_id = :id", nativeQuery = true)
    List<Rating> getTotalRatingStartOfFood(@Param("id") Long foodId);
}
