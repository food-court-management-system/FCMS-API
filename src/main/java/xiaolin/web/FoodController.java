package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.FoodDto;
import xiaolin.entities.Food;
import xiaolin.entities.Rating;
import xiaolin.services.IFoodService;
import xiaolin.services.IRatingService;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    IFoodService foodService;

    @ResponseBody
    @RequestMapping(value = "/filter/top-food/lists", method = RequestMethod.GET)
    public ResponseEntity<Object> getTopRatingFood() {
        return new ResponseEntity<>(foodService.getTopFoodOfFoodCourt(), HttpStatus.OK);
    }
}
