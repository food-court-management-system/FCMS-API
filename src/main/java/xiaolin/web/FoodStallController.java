package xiaolin.web;

import com.amazonaws.services.s3.model.Region;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xiaolin.dtos.FoodDto;
import xiaolin.dtos.food.FoodCreateDTO;
import xiaolin.dtos.foodstall.FoodStallDetailDTO;
import xiaolin.entities.Food;
import xiaolin.entities.FoodStall;
import xiaolin.entities.Type;
import xiaolin.services.IFoodService;
import xiaolin.services.IFoodStallService;
import xiaolin.services.ITypeService;
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

    @Autowired
    ITypeService typeService;

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
        List<FoodStall> foodStallList = foodStallService.listAllActiveFoodStall();
        List<FoodStallDetailDTO> result = new ArrayList<>();
        for (FoodStall foodStall: foodStallList) {
            FoodStallDetailDTO foodStallDetailDTO = new FoodStallDetailDTO();
            foodStallDetailDTO.setFoodStallName(foodStall.getFoodStallName());
            foodStallDetailDTO.setFoodStallId(foodStall.getFoodStallId());
            foodStallDetailDTO.setFoodStallDescription(null);
            foodStallDetailDTO.setFoodStallRating(foodStall.getFoodStallRating());
            result.add(foodStallDetailDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> searchFoodStallByName(@RequestParam(value = "name", required = false) String foodStallName) {
        List<FoodStall> foodStallList;
        if (foodStallName == null) {
            foodStallList = foodStallService.listAllActiveFoodStall();
        } else {
            foodStallList = foodStallService.searchFoodStallByName(foodStallName);
        }
        return new ResponseEntity<>(foodStallList, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> createNewFoodStallOfFoodCourt(@RequestParam("image") MultipartFile fileImage,
                                                                @ModelAttribute FoodStallDetailDTO foodStallDetailDTO) {
        JsonObject jsonObject = new JsonObject();
        if (fileImage == null) {
            jsonObject.addProperty("message", "Food stall image must have in your food stall");
            return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
        }
        if (foodStallDetailDTO.getFoodStallName().length() == 0) {
            jsonObject.addProperty("message", "Food stall name must have in your food stall");
            return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
        }
        if (foodStallDetailDTO.getFoodStallDescription().length() == 0) {
            jsonObject.addProperty("message", "Food stall description must have in your food stall");
            return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
        }
        FoodStall foodStall = new FoodStall();
        foodStall.setFoodStallDescription(foodStallDetailDTO.getFoodStallDescription());
        foodStall.setFoodStallName(foodStallDetailDTO.getFoodStallName());
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
            String fileImageName = foodStallDetailDTO.getFoodStallName() + "_" + currentTime + ".png";
            Path path = Paths.get(UPLOAD_FOLDER + fileImageName);
            // image saving file
            byte[] bytes = fileImage.getBytes();
            Files.write(path, bytes);

            // upload to S3
            Uploader uploader = new S3Uploader(accessKey, secretKey, bucket, Region.AP_Singapore.toString());
            String url = uploader.upload(new File(path.toString()));
            foodStall.setFoodStallImage(url);

            // clean up
            folder.listFiles()[0].delete();
            folder.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        FoodStall result = foodStallService.saveFoodStallToDB(foodStall);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id:\\d+}/edit", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> editFoodStallInfo(@PathVariable("id") Long foodStallId,
                                                    @RequestParam(value = "image", required = false) MultipartFile fileImage,
                                                    @ModelAttribute FoodStallDetailDTO foodStallDetailDTO) {
        JsonObject jsonObject = new JsonObject();
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallId);
        if (foodStall == null) {
            jsonObject.addProperty("message", "Cannot find food stall with that id");
            return new ResponseEntity<>(jsonObject, HttpStatus.NOT_FOUND);
        }
        if (foodStallDetailDTO.getFoodStallName().length() != 0) {
            foodStall.setFoodStallName(foodStallDetailDTO.getFoodStallName());
        }
        if (foodStallDetailDTO.getFoodStallDescription().length() != 0) {
            foodStall.setFoodStallDescription(foodStallDetailDTO.getFoodStallDescription());
        }
        Long currentTime = new Date().getTime();
        if (fileImage != null) {
            try {
                // create new folder tmp for saving image
                File folder = new File(UPLOAD_FOLDER);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                // make a name for image (name of food stall_bigint)
                String fileImageName = foodStallDetailDTO.getFoodStallName() + "_" + currentTime + ".png";
                Path path = Paths.get(UPLOAD_FOLDER + fileImageName);
                // image saving file
                byte[] bytes = fileImage.getBytes();
                Files.write(path, bytes);

                // upload to S3
                Uploader uploader = new S3Uploader(accessKey, secretKey, bucket, Region.AP_Singapore.toString());
                String url = uploader.upload(new File(path.toString()));
                foodStall.setFoodStallImage(url);

                // clean up
                folder.listFiles()[0].delete();
                folder.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FoodStall result = foodStallService.saveFoodStallToDB(foodStall);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deactiveFoodStallOfFoodcourt(@PathVariable("id") Long foodStallId) {
        JsonObject jsonObject = new JsonObject();
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallId);
        if (foodStall == null) {
            jsonObject.addProperty("message", "Cannot find food stall with that id");
            return new ResponseEntity<>(jsonObject, HttpStatus.NOT_FOUND);
        }
        foodStall.setActive(false);
        FoodStall result = foodStallService.saveFoodStallToDB(foodStall);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/filter/top-food-stall", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getTopFoodStall() {
        return new ResponseEntity<>(foodStallService.getTopFoodStallOfFoodCourt(), HttpStatus.OK);
    }

    @RequestMapping(value = "/filter/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> filterFoodStallByCategory(@PathVariable("tag") String category) {
        return new ResponseEntity<>(foodStallService.filterFoodStallByCategory(category), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:\\d+}/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getFoodStallDetail(@PathVariable("id") Long id) {
        FoodStall result = foodStallService.getFoodStallDetail(id);
        if (result == null) {
            System.out.println("Null result");
            return new ResponseEntity<>(
                    FCMSUtil.returnErrorMsg("Cannot found food stall", HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:\\d+}/add/food", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> addNewFood(@PathVariable("id") Long foodStallId,
                                            @RequestBody FoodCreateDTO listFoodDTO) {
        JsonObject jsonObject = new JsonObject();
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallId);
        if (foodStall == null) {
            jsonObject.addProperty("message", "Cannot found that food stall id");
            return new ResponseEntity<>(jsonObject, HttpStatus.NOT_FOUND);
        }
//        if (listFoodDTO.length == 0 ) {
//            jsonObject.addProperty("message", "Don't have food to add");
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
        List<Food> listFoodResult = new ArrayList<>();
//        for(int i = 0; i < listFoodDTO.length; i++) {
            Food food = new Food();
            food.setFoodDescription(listFoodDTO.getFoodDescription());
            food.setFoodName(listFoodDTO.getFoodName());
            food.setOriginPrice(listFoodDTO.getOriginPrice());
            food.setRetailPrice(listFoodDTO.getRetailPrice());
            food.setFoodStall(foodStall);
            Type foodType = typeService.getTypeBaseOnTypeName(listFoodDTO.getFoodType());
            food.setFoodType(foodType);

            Food foodResult =foodService.saveFood(food);
            if (foodResult != null) {
                listFoodResult.add(foodResult);
            }
//        }
        if (foodResult != null) {
            return new ResponseEntity<>(foodResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id:\\d+}/food/{food-id}", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/{id:\\d+}/food/{food-id}/edit", method = RequestMethod.PUT)
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
