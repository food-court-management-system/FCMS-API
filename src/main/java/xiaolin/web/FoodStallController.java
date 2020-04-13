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
import xiaolin.dtos.CancelOrderDetailDTO;
import xiaolin.dtos.food.FoodCreateDTO;
import xiaolin.dtos.foodstall.FoodStallDetailDTO;
import xiaolin.dtos.user.UserDetailDTO;
import xiaolin.entities.*;
import xiaolin.services.*;
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

    @Autowired
    IUserService userService;

    @Autowired
    ICartService cartService;

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
            foodStallDetailDTO.setFoodStallImage(foodStall.getFoodStallImage());
            foodStallDetailDTO.setFoodStallId(foodStall.getFoodStallId());
            foodStallDetailDTO.setFoodStallDescription(null);
            foodStallDetailDTO.setFoodStallRating(foodStall.getFoodStallRating());
            result.add(foodStallDetailDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<Object> searchFoodStallByName(@RequestParam(value = "name", required = false) String foodStallName) {
//        List<FoodStall> foodStallList;
//        if (foodStallName == null) {
//            foodStallList = foodStallService.listAllActiveFoodStall();
//        } else {
//            foodStallList = foodStallService.searchFoodStallByName(foodStallName);
//        }
//        return new ResponseEntity<>(foodStallList, HttpStatus.OK);
//    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> createNewFoodStallOfFoodCourt(@RequestParam("image") MultipartFile fileImage,
                                                                @ModelAttribute FoodStallDetailDTO foodStallDetailDTO) {
        JsonObject jsonObject = new JsonObject();
        if (fileImage == null) {
            jsonObject.addProperty("message", "Food stall image must have in your food stall");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (foodStallDetailDTO.getFoodStallName() == null) {
            jsonObject.addProperty("message", "Food stall name must have in your food stall");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (foodStallDetailDTO.getFoodStallDescription() == null) {
            jsonObject.addProperty("message", "Food stall description must have in your food stall");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
        }
        if (foodStallDetailDTO.getFoodStallName() != null) {
            foodStall.setFoodStallName(foodStallDetailDTO.getFoodStallName());
        }
        if (foodStallDetailDTO.getFoodStallDescription() != null) {
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
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
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
        List<FoodStall> foodStallList = foodStallService.listAllActiveFoodStall();
        List<FoodStallDetailDTO> result = new ArrayList<>();
        for (FoodStall foodStall: foodStallList) {
            FoodStallDetailDTO foodStallDetailDTO = new FoodStallDetailDTO();
            foodStallDetailDTO.setFoodStallName(foodStall.getFoodStallName());
            foodStallDetailDTO.setFoodStallImage(foodStall.getFoodStallImage());
            foodStallDetailDTO.setFoodStallId(foodStall.getFoodStallId());
            foodStallDetailDTO.setFoodStallDescription(null);
            foodStallDetailDTO.setFoodStallRating(foodStall.getFoodStallRating());
            result.add(foodStallDetailDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
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

    @ResponseBody
    @RequestMapping(value = "/{id:\\d+}/detail/menu", method = RequestMethod.GET)
    public ResponseEntity<Object> getFoodStallMenu(@PathVariable("id") Long foodStallId){
        return new ResponseEntity<>(foodService.getFoodStallMenu(foodStallId), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:\\d+}/add/food", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ResponseEntity<Object> addNewFood(@PathVariable("id") Long foodStallId,
                                             @RequestParam("image") MultipartFile foodCourtImage,
                                             @ModelAttribute FoodCreateDTO foodDTO) {
        JsonObject jsonObject = new JsonObject();
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallId);
        if (foodStall == null) {
            jsonObject.addProperty("message", "Cannot found that food stall id");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
        }
        if (foodDTO.getFoodName() == null) {
            jsonObject.addProperty("message", "Food name cannot be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (foodDTO.getFoodDescription() == null) {
            jsonObject.addProperty("message", "Food description cannot be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (foodDTO.getFoodType() == null) {
            jsonObject.addProperty("message", "Food type cannot be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (foodDTO.getOriginPrice() == null) {
            jsonObject.addProperty("message", "Food origin price cannot be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (foodDTO.getRetailPrice() == null) {
            jsonObject.addProperty("message", "Food retail price cannot be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        Type foodType = typeService.getTypeBaseOnTypeName(foodDTO.getFoodType());
        Food food = new Food();
        food.setFoodStall(foodStall);
        food.setFoodType(foodType);
        food.setFoodDescription(foodDTO.getFoodDescription());
        food.setOriginPrice(foodDTO.getOriginPrice());
        food.setRetailPrice(foodDTO.getRetailPrice());
        food.setFoodName(foodDTO.getFoodName());
        Long currentTime = new Date().getTime();
        try {
            // create new folder tmp for saving image
            File folder = new File(UPLOAD_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // make a name for image (name of food stall_bigint)
            String fileImageName = foodDTO.getFoodName() + "_" + currentTime + ".png";
            Path path = Paths.get(UPLOAD_FOLDER + fileImageName);
            // image saving file
            byte[] bytes = foodCourtImage.getBytes();
            Files.write(path, bytes);

            // upload to S3
            Uploader uploader = new S3Uploader(accessKey, secretKey, bucket, Region.AP_Singapore.toString());
            String url = uploader.upload(new File(path.toString()));
            food.setFoodImage(url);

            // clean up
            folder.listFiles()[0].delete();
            folder.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        food.setFoodRating(0);
        food.setIsActive(true);
        Food result = foodService.saveFood(food);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id:\\d+}/food/{food-id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Object> deleteFood(@PathVariable("id") Long foodStallId,
                                             @PathVariable("food-id") Long foodId) {
        JsonObject jsonObject = new JsonObject();
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallId);
        if (foodStall == null) {
            jsonObject.addProperty("message", "Cannot find that food stall");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        Food food = foodService.getFoodDetailById(foodId);
        if (food == null) {
            jsonObject.addProperty("message", "Cannot find that food in food stall");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        food.setIsActive(false);
        Food result = foodService.saveFood(food);
        if (result != null) {
            jsonObject.addProperty("message", "Successfully delete");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } else {
            jsonObject.addProperty("message", "Successfully failed");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id:\\d+}/food/{food-id}/edit", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Object> updateFood(@PathVariable("id") Long foodStallId,
                                             @PathVariable("food-id") Long foodId,
                                             @RequestParam(value = "image", required = false) MultipartFile foodCourtImage,
                                             @ModelAttribute FoodCreateDTO foodDTO) {
        JsonObject jsonObject = new JsonObject();
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallId);
        Food food = foodService.getFoodDetailById(foodId);
        if (food == null) {
            jsonObject.addProperty("message", "Cannot found that food");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (foodStall == null) {
            jsonObject.addProperty("message", "Cannot found that food stall id");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
        }
        if (foodDTO.getFoodName() != null) {
            food.setFoodName(foodDTO.getFoodName());
        }
        if (foodDTO.getFoodDescription() != null) {
            food.setFoodDescription(foodDTO.getFoodDescription());
        }
        if (foodDTO.getFoodType() != null) {
            Type foodType = typeService.getTypeBaseOnTypeName(foodDTO.getFoodType());
            food.setFoodType(foodType);
        }
        if (foodDTO.getOriginPrice() != null) {
            food.setOriginPrice(foodDTO.getOriginPrice());
        }
        if (foodDTO.getRetailPrice() != null) {
            food.setRetailPrice(foodDTO.getRetailPrice());
        }
        if (foodCourtImage != null) {
            Long currentTime = new Date().getTime();
            try {
                // create new folder tmp for saving image
                File folder = new File(UPLOAD_FOLDER);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                // make a name for image (name of food stall_bigint)
                String fileImageName = foodDTO.getFoodName() + "_" + currentTime + ".png";
                Path path = Paths.get(UPLOAD_FOLDER + fileImageName);
                // image saving file
                byte[] bytes = foodCourtImage.getBytes();
                Files.write(path, bytes);

                // upload to S3
                Uploader uploader = new S3Uploader(accessKey, secretKey, bucket, Region.AP_Singapore.toString());
                String url = uploader.upload(new File(path.toString()));
                food.setFoodImage(url);

                // clean up
                folder.listFiles()[0].delete();
                folder.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Food result = foodService.saveFood(food);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/food/{food-id}/detail", method = RequestMethod.GET)
    public ResponseEntity<Object> getFoodDetailById(@PathVariable("id") Long foodStallId,
                                                    @PathVariable("food-id") Long foodId) {
        JsonObject jsonObject = new JsonObject();
        if (foodStallId == null) {
            jsonObject.addProperty("message", "Cannot found that food stall");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        if (foodId == null) {
            jsonObject.addProperty("message", "Cannot found that food");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        Food result = foodService.getFoodDetailById(foodId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/food-stall-staff/list", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFoodStallStaffOfFoodStall(@PathVariable("id") Long foodStallId) {
        List<User> staffList = userService.getAllFoodStallStaffOfFoodStall(foodStallId );
        List<UserDetailDTO> foodStallStaffListResult = new ArrayList<>();
        for (User staff: staffList) {
            UserDetailDTO foodStallStaff = new UserDetailDTO();
            foodStallStaff.setUserId(staff.getId());
            foodStallStaff.setFirstName(staff.getFirstName());
            foodStallStaff.setLastName(staff.getLastName());
            foodStallStaff.setAge(staff.getAge());
            foodStallStaff.setUsername(staff.getUserName());
            foodStallStaffListResult.add(foodStallStaff);
        }
        return new ResponseEntity<>(foodStallStaffListResult, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Object> getFoodStallBaseOnFoodName(@RequestParam(value = "name") String foodName) {
        List<FoodStall> foodStallList;
        if (foodName.length() == 0) {
            foodStallList = foodStallService.listAllActiveFoodStall();
        } else {
            foodStallList = foodStallService.searchFoodStallBaseOnFoodName(foodName);
        }
        List<FoodStallDetailDTO> result = new ArrayList<>();
        for(FoodStall foodStall: foodStallList) {
            FoodStallDetailDTO dto = new FoodStallDetailDTO();
            dto.setFoodStallDescription(null);
            dto.setFoodStallName(foodStall.getFoodStallName());
            dto.setFoodStallId(foodStall.getFoodStallId());
            dto.setFoodStallImage(foodStall.getFoodStallImage());
            dto.setFoodStallRating(foodStall.getFoodStallRating());
            result.add(dto);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/{id}/cancel-orders", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCancelOrders(@PathVariable("id") Long foodStallId) {
        List<Food> menu = foodService.getFoodStallMenu(foodStallId);
        List<CancelOrderDetailDTO> result = new ArrayList<>();
        List<Long> foodIds = new ArrayList<>();
        for(Food f: menu) {
            foodIds.add(f.getId());
        }

        List<CartItem> cancelCartItems = cartService.getAllCancelCartItemInFoodStall(foodIds);
        for(CartItem ci: cancelCartItems) {
            CancelOrderDetailDTO cancelOrderDetailDTO = new CancelOrderDetailDTO();
            cancelOrderDetailDTO.setId(ci.getId());
            cancelOrderDetailDTO.setReason(ci.getReason());
            cancelOrderDetailDTO.setPurchasedPrice(ci.getPurchasedPrice());
            cancelOrderDetailDTO.setQuantity(ci.getQuantity());
            cancelOrderDetailDTO.setPurchasedDate(ci.getCart().getPurchaseDate().toString());
            cancelOrderDetailDTO.setFoodName(ci.getFoodId().getFoodName());
            result.add(cancelOrderDetailDTO);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
