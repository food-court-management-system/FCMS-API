package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xiaolin.dtos.CustomerDto;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.entities.Customer;
import xiaolin.services.ICartService;
import xiaolin.util.FCMSUtil;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    @ResponseBody
    @RequestMapping(value = "/history/{id:\\d+}/detail")
    public ResponseEntity<Object> getHistoryOrder(@PathVariable("id") Long customerId) {
        return new ResponseEntity<>(cartService.getHistoryOrder(customerId),HttpStatus.OK);
    }
}
