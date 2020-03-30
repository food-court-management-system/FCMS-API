package xiaolin.web;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.UserDto;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.dtos.user.UserCreateDTO;
import xiaolin.entities.FoodCourtInformation;
import xiaolin.entities.User;
import xiaolin.services.IFoodCourtService;
import xiaolin.services.IUserService;
import xiaolin.util.FCMSUtil;

import java.util.List;

@RestController
@RequestMapping("food-court")
public class FoodCourtController {

    @Autowired
    IFoodCourtService foodCourtService;

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getFoodCourtInformation() {
        return new ResponseEntity<>(foodCourtService.getFoodCourtInformation(), HttpStatus.OK);
    }

    @RequestMapping(value = "/cashier/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> createCahsierOfFoodCourt(@RequestBody UserCreateDTO cashierDTO) {
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
        for (User cashier: cashierList) {
            cashier.setPassword(null);
        }
        return new ResponseEntity<>(cashierList, HttpStatus.OK);
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

    @RequestMapping(value = "/information", method = RequestMethod.POST)
    @ResponseBody
    public FoodCourtInformation createFoodCourtInformation(@RequestBody FoodCourtInformation foodCourtInformation) {
        return foodCourtService.saveFoodCourtInformation(foodCourtInformation);
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
}
