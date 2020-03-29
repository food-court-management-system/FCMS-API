package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xiaolin.dtos.FoodDto;
import xiaolin.dtos.FoodStallCreateDTO;
import xiaolin.dtos.food.FoodStallInfoDTO;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.entities.Food;
import xiaolin.entities.FoodStall;
import xiaolin.services.IFoodService;
import xiaolin.services.IFoodStallService;
import xiaolin.util.FCMSUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/food-stall")
public class FoodStallController {

    @Autowired
    IFoodStallService foodStallService;

    @Autowired
    IFoodService foodService;

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> listAllActiveFoodStall() {
        return new ResponseEntity<>(foodStallService.listAllActiveFoodStall(), HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> searchFoodStallByName(@RequestParam("name") String foodStallName) {
        return new ResponseEntity<>(foodStallService.searchFoodStallByName(foodStallName), HttpStatus.OK);
    }

    @RequestMapping(value = "/filter/top-food-stall", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getTopFoodStall() {
        List<FoodStallInfoDTO> result = new ArrayList<>();
//        List<FoodStall> topFoodStall = foodStallService.getTopFoodStallOfFoodCourt();
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> createNewFoodStallOfFoodCourt(@RequestParam("image") MultipartFile fileImage,
                                                                @ModelAttribute FoodStallCreateDTO foodStallCreateDTO) {
        if (fileImage == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        FoodStall foodStall = new FoodStall();
        foodStall.setFoodStallName(foodStallCreateDTO.getFoodStallName());
        foodStall.setFoodStallDescription(foodStallCreateDTO.getFoodStallDescription());
        foodStall.setFoodStallRating(0);
        foodStall.setActive(true);
        Long currentTime = new Date().getTime();
        try {
            // create new folder tmp for saving image
            File imageDestination = new File("tmp");
            // make a name for image (name of food stall_bigint)
            String fileImageName = foodStallCreateDTO.getFoodStallName() + "_" + currentTime + ".png";
            if (!imageDestination.exists()) {
                imageDestination.mkdir();
            }
            // get the absolute path of project to saving image
            String imageAbsolutePath = imageDestination.getAbsolutePath() + "\\" + fileImageName;
            // image saving file
            imageDestination = new File(imageAbsolutePath);
            // transfer data of image to new file image in folder tmp
            fileImage.transferTo(imageDestination);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
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

    @RequestMapping(value = "/{id:\\d+}/detail", method = RequestMethod.GET)
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
            return new ResponseEntity<>(FCMSUtil.returnErrorMsg("Missing food", HttpStatus.NOT_ACCEPTABLE), HttpStatus.NOT_ACCEPTABLE);
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
