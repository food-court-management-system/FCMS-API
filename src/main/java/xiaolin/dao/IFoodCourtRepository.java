package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xiaolin.entities.FoodCourtInformation;

@Repository
public interface IFoodCourtRepository extends JpaRepository<FoodCourtInformation, Long> {


    FoodCourtInformation findFirstByOrderByFoodCourtIdDesc();



//    FoodCourtInformation editFoodCourtInformation();
}
