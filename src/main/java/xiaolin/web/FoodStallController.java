package xiaolin.web;

import com.amazonaws.services.s3.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import xiaolin.uploader.S3Uploader;
import xiaolin.uploader.Uploader;
import xiaolin.util.FCMSUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/food-stall")
public class FoodStallController {

    @Autowired
    IFoodStallService foodStallService;

    @Autowired
    IFoodService foodService;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Value("${amazonProperties.bucket}")
    private String bucket;

    private static final String UPLOAD_FOLDER = "temp/";

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
            File folder = new File(UPLOAD_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // make a name for image (name of food stall_bigint)
            String fileImageName = foodStallCreateDTO.getFoodStallName() + "_" + currentTime + ".png";
            Path path = Paths.get(UPLOAD_FOLDER + fileImageName);
            // image saving file
            byte[] bytes = fileImage.getBytes();
            Files.write(path, bytes);

            // upload to S3
            Uploader uploader = new S3Uploader(accessKey, secretKey, bucket, Region.AP_Singapore.toString());
            String url = uploader.upload(new File(path.toString()));
            foodStall.setFoodStallImage(url);

            // save food stall
            

            // clean up
            folder.listFiles()[0].delete();
            folder.delete();

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
