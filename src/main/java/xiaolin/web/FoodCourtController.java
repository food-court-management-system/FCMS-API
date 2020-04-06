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
import xiaolin.dtos.FoodCourtDetailInfoDTO;
import xiaolin.dtos.FoodCourtInformationDto;
import xiaolin.dtos.user.FoodStallManagerDetailDTO;
import xiaolin.dtos.user.FoodStallUserCreateDTO;
import xiaolin.dtos.user.UserCreateDTO;
import xiaolin.dtos.user.UserDetailDTO;
import xiaolin.entities.FoodCourtInformation;
import xiaolin.entities.FoodStall;
import xiaolin.entities.Type;
import xiaolin.entities.User;
import xiaolin.services.IFoodCourtService;
import xiaolin.services.IFoodStallService;
import xiaolin.services.ITypeService;
import xiaolin.services.IUserService;
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
@RequestMapping("food-court")
public class FoodCourtController {

    @Autowired
    IFoodCourtService foodCourtService;

    @Autowired
    IUserService userService;

    @Autowired
    ITypeService typeService;

    @Autowired
    IFoodStallService foodStallService;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Value("${amazonProperties.bucket}")
    private String bucket;

    private static final String UPLOAD_FOLDER = "temp/";

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getFoodCourtInformation() {
        return new ResponseEntity<>(foodCourtService.getFoodCourtInformation(), HttpStatus.OK);
    }

    @RequestMapping(value = "/cashier/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> createCashierOfFoodCourt(@RequestBody UserCreateDTO cashierDTO) {
        User cashier = new User();
        User user = userService.getUserInfo(cashierDTO.getUsername());
        JsonObject jsonObject = new JsonObject();
        if (user != null) {
            jsonObject.addProperty("message", "This username is existed please use another username");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
        cashier.setUserName(cashierDTO.getUsername());
        cashier.setPassword(FCMSUtil.encodePassword(cashierDTO.getPassword()));
        cashier.setFirstName(cashierDTO.getFirstName());
        cashier.setLastName(cashierDTO.getLastName());
        cashier.setAge(cashierDTO.getAge());
        cashier.setActive(true);
        cashier.setRole("cashier");
        User result = userService.saveUser(cashier);
        if (result != null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/cashier/lists", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> listAllCashierOfFoodCourt() {
        List<User> cashierList = userService.getAllUserOfFoodCourtBaseOnRole("cashier");
        List<UserDetailDTO> cashierListResult = new ArrayList<>();
        for (User cashier: cashierList) {
            UserDetailDTO cashierUser = new UserDetailDTO();
            cashierUser.setUserId(cashier.getId());
            cashierUser.setFirstName(cashier.getFirstName());
            cashierUser.setLastName(cashier.getLastName());
            cashierUser.setAge(cashier.getAge());
            cashierUser.setUsername(cashier.getUserName());
            cashierListResult.add(cashierUser);
        }
        return new ResponseEntity<>(cashierListResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/cashier/{id}/delete", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Object> deleteCashierOfFoodCourt(@PathVariable("id") Long cashierId) {
        User cashier = userService.getUserInformation(cashierId);
        if (cashier != null) {
            cashier.setActive(false);
            User result = userService.saveUser(cashier);
            if (result != null) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/information", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ResponseEntity<Object> createFoodCourtInformation(@RequestParam(value = "image", required = false) MultipartFile image,
                                                             @ModelAttribute FoodCourtDetailInfoDTO foodCourtDetailInfoDTO) {
        FoodCourtInformation foodCourtInformation = foodCourtService.getFoodCourtInformation();
        if (foodCourtDetailInfoDTO.getFoodCourtName().length() != 0) {
            foodCourtInformation.setFoodCourtName(foodCourtDetailInfoDTO.getFoodCourtName());
        }
        if (foodCourtDetailInfoDTO.getFoodCourtAddress().length() != 0) {
            foodCourtInformation.setFoodCourtAddress(foodCourtDetailInfoDTO.getFoodCourtAddress());
        }
        if (foodCourtDetailInfoDTO.getFoodCourtDescription().length() != 0) {
            foodCourtInformation.setFoodCourtDescription(foodCourtDetailInfoDTO.getFoodCourtDescription());
        }
        Long currentTime = new Date().getTime();
        if (!image.isEmpty()) {
            try {
                // create new folder tmp for saving image
                File folder = new File(UPLOAD_FOLDER);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                // make a name for image (name of food stall_bigint)
                String fileImageName = foodCourtDetailInfoDTO.getFoodCourtName() + "_" + currentTime + ".png";
                Path path = Paths.get(UPLOAD_FOLDER + fileImageName);
                // image saving file
                byte[] bytes = image.getBytes();
                Files.write(path, bytes);

                // upload to S3
                Uploader uploader = new S3Uploader(accessKey, secretKey, bucket, Region.AP_Singapore.toString());
                String url = uploader.upload(new File(path.toString()));
                foodCourtInformation.setFoodCourtImage(url);

                // clean up
                folder.listFiles()[0].delete();
                folder.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FoodCourtInformation result = foodCourtService.saveFoodCourtInformation(foodCourtInformation);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/food-stall-manager/{id}/delete", method = RequestMethod.PUT)
    public ResponseEntity<Object> deleteFoodStallManagerOfFoodCourt(@PathVariable("id") Long foodStallManagerId) {
        User foodStallManager = userService.getUserInformation(foodStallManagerId);
        if (foodStallManager != null) {
            foodStallManager.setActive(false);
            User result = userService.saveUser(foodStallManager);
            if (result != null) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/food-stall-manager/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createFoodStallManagerOfFoodCourt(@RequestBody FoodStallUserCreateDTO foodStallManagerDTO) {
        User foodStallManager = new User();
        User user = userService.getUserInfo(foodStallManagerDTO.getUsername());
        JsonObject jsonObject = new JsonObject();
        if (user != null) {
            jsonObject.addProperty("message", "This username is existed please use another username");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
        foodStallManager.setUserName(foodStallManagerDTO.getUsername());
        foodStallManager.setPassword(FCMSUtil.encodePassword(foodStallManagerDTO.getPassword()));
        foodStallManager.setFirstName(foodStallManagerDTO.getFirstName());
        foodStallManager.setLastName(foodStallManagerDTO.getLastName());
        foodStallManager.setAge(foodStallManagerDTO.getAge());
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallManagerDTO.getFoodStallId());
        if (foodStall != null) {
            foodStallManager.setFoodStall(foodStall);
        }
        foodStallManager.setActive(true);

        foodStallManager.setRole("fsmanager");
        User result = userService.saveUser(foodStallManager);
        if (result != null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/food-stall-manager/lists", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> listAllFoodStallManager() {
        List<User> foodStallManagerList = userService.getAllUserOfFoodCourtBaseOnRole("fsmanager");
        List<FoodStallManagerDetailDTO> foodStallManagerListResult = new ArrayList<>();
        for (User manager: foodStallManagerList) {
            FoodStallManagerDetailDTO foodStallManager = new FoodStallManagerDetailDTO();
            foodStallManager.setId(manager.getId());
            foodStallManager.setFirstname(manager.getFirstName());
            foodStallManager.setLastname(manager.getLastName());
            foodStallManager.setAge(manager.getAge());
            foodStallManager.setUsername(manager.getUserName());
            FoodStall foodStallOwner = foodStallService.getFoodStallDetail(manager.getFoodStall().getFoodStallId());
            if (foodStallOwner != null) {
                foodStallManager.setFsid(foodStallOwner.getFoodStallId());
                foodStallManager.setFsname(foodStallOwner.getFoodStallName());
            }
            foodStallManagerListResult.add(foodStallManager);
        }
        return new ResponseEntity<>(foodStallManagerListResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/food-stall-staff/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> createFoodStallStaffOfFoodCourt(@RequestBody FoodStallUserCreateDTO foodStallStaffDTO) {
        User foodStallStaff = new User();
        User user = userService.getUserInfo(foodStallStaffDTO.getUsername());
        JsonObject jsonObject = new JsonObject();
        if (user != null) {
            jsonObject.addProperty("message", "This username is existed please use another username");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
        foodStallStaff.setUserName(foodStallStaffDTO.getUsername());
        foodStallStaff.setPassword(FCMSUtil.encodePassword(foodStallStaffDTO.getPassword()));
        foodStallStaff.setFirstName(foodStallStaffDTO.getFirstName());
        foodStallStaff.setLastName(foodStallStaffDTO.getLastName());
        foodStallStaff.setAge(foodStallStaffDTO.getAge());
        FoodStall foodStall = foodStallService.getFoodStallDetail(foodStallStaffDTO.getFoodStallId());
        if (foodStall != null) {
            foodStallStaff.setFoodStall(foodStall);
        }
        foodStallStaff.setActive(true);

        foodStallStaff.setRole("fsstaff");
        User result = userService.saveUser(foodStallStaff);
        if (result != null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/food-stall-staff/lists", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> listAllFoodStallStaff() {
        List<User> cashierList = userService.getAllUserOfFoodCourtBaseOnRole("fsstaff");
        List<UserDetailDTO> foodStallStaffListResult = new ArrayList<>();
        for (User staff: cashierList) {
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

    @RequestMapping(value = "/food-stall-staff/{id:\\d+}/delete", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Object> deleteFoodStallStaff(@PathVariable("id") Long foodStallStaffId) {
        User foodStallStaff = userService.getUserInformation(foodStallStaffId);
        if (foodStallStaff != null) {
            foodStallStaff.setActive(false);
            User result = userService.saveUser(foodStallStaff);
            if (result != null) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/type/new-type", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewFoodTypeInFoodCourt(@RequestParam("name") String typeName){
        JsonObject jsonObject = new JsonObject();
        if (typeName == null) {
            jsonObject.addProperty("message", "Type name cannot be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        Type newType = new Type();
        newType.setTypeName(typeName);
        Type result = typeService.addNewTypeToFoodCourt(newType);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/type/lists", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFoodTypeInFoodCourt() {
        return new ResponseEntity<>(typeService.getAllTypesInFoodCourt(), HttpStatus.OK);
    }
}
