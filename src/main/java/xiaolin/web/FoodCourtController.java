package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.UserDto;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.entities.User;
import xiaolin.services.IFoodCourtService;

@RestController
public class FoodCourtController {

    @Autowired
    IFoodCourtService foodCourtService;

//    @Autowired


    @RequestMapping(value = "/api/v1/food-court/about", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getFoodCourtInformation() {
        return new ResponseEntity<>(foodCourtService.getFoodCourtInformation(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/food-court/create/cashier", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> createNewCashier(@RequestBody UserDto dto) {
        if (dto.getUsername() != null) {
            return null;
        }
        dto.setRole("cashier");
        User user = FCMSMapper.mapToUser(dto);
        return null;
    }
}
