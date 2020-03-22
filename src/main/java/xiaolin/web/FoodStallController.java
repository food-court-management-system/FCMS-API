package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.FoodDto;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.entities.Food;
import xiaolin.entities.FoodStall;
import xiaolin.services.IFoodService;
import xiaolin.services.IFoodStallService;
import xiaolin.util.FCMSUtil;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/stall")
public class FoodStallController {

    @Autowired
    IFoodStallService foodStallService;

    @Autowired
    IFoodService foodService;

    @RequestMapping(value = "/food-stalls", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> listAllFoodStall() {
        return new ResponseEntity<>(foodStallService.listAllFoodStall(), HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> searchFoodStall(@RequestParam("name") String foodStallName) {
        return new ResponseEntity<>(foodStallService.searchFoodStallByName(foodStallName), HttpStatus.OK);
    }

    @RequestMapping(value = "/filter/rate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> filterFoodStallByRating() {
        return new ResponseEntity<>(foodStallService.filterFoodStallByRating(), HttpStatus.OK);
    }

    @RequestMapping(value = "/filter/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> filterFoodStallByCategory(@PathVariable("tag") String category) {
        return new ResponseEntity<>(foodStallService.filterFoodStallByCategory(category), HttpStatus.OK);
    }

    @RequestMapping(value = "/food-stalls/{id:\\d+}/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getFoodStallDetail(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(
                    FCMSUtil.returnErrorMsg("Cannot found food stall with that id", HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
        FoodStall result = foodStallService.getFoodStallDetail(id);
        if (result == null) {
            System.out.println("Null result");
            return new ResponseEntity<>(
                    FCMSUtil.returnErrorMsg("Cannot found food stall", HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:\\d+}/add-food", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> addNewFood(@PathVariable("id") Long foodStallId,
                        @RequestBody FoodDto dto) {
        if (foodStallId == null) {
            return new ResponseEntity<>(FCMSUtil.returnErrorMsg("Can't found that food stall",HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (dto == null) {
            return new ResponseEntity<>(FCMSUtil.returnErrorMsg("Missing Food", HttpStatus.NOT_ACCEPTABLE), HttpStatus.NOT_ACCEPTABLE);
        }
        Food food = FCMSMapper.mapToFood(dto);
        return new ResponseEntity<>(foodService.addNewFood(food), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:\\d+}/foods/{food-id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Object> deleteFood(@PathVariable("id") Long foodStallId,
                                             @PathVariable("food-id") Long foodId) {
        if (foodStallId == null) {
            return new ResponseEntity<>(FCMSUtil.returnErrorMsg("Can't found that food stall",HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (foodId == null) {
            return new ResponseEntity<>(FCMSUtil.returnErrorMsg("Can't found that food",HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        foodService.deleteFood(foodId);
        return new ResponseEntity<>("Successfully delete" ,HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:\\d+}/foods/{food-id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Object> updateFood(@PathVariable("id") Long foodStallId,
                                             @PathVariable("food-id") Long foodId) {
        if (foodStallId == null) {
            return new ResponseEntity<>(FCMSUtil.returnErrorMsg("Can't found that food stall",HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (foodId == null) {
            return new ResponseEntity<>(FCMSUtil.returnErrorMsg("Can't found that food",HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        foodService.deleteFood(foodId);
        return new ResponseEntity<>("Successfully delete" ,HttpStatus.OK);
    }
}
