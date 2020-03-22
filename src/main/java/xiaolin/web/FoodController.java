package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.FoodDto;
import xiaolin.services.IFoodService;

@RestController
public class FoodController {

    @Autowired
    private IFoodService foodService;

    @RequestMapping(value = "/api/v1/food/add", method = RequestMethod.POST)
    @ResponseBody
    public boolean addNewFood(@RequestBody FoodDto dto) {
        boolean result = true;


        return result;
    }

}
