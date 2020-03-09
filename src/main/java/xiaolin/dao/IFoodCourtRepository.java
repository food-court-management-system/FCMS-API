package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xiaolin.entities.FoodCourtInformation;

@Repository
public interface IFoodCourtRepository extends JpaRepository<FoodCourtInformation, Long> {

    @Query(value = "SELECT fc FROM FoodCourtInformation fc WHERE fc.foodCourtId = 1")
    FoodCourtInformation getFoodCourtInformation();

//    FoodCourtInformation editFoodCourtInformation();
}
