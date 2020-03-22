package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xiaolin.entities.Rating;
import xiaolin.services.ICustomerService;
//import xiaolin.services.IRatingService;

@RestController
public class CustomerController {

    @Autowired
    ICustomerService customerService;

//    @Autowired
//    IRatingService ratingService;

//    @ResponseBody
//    @RequestMapping(value = "/api/v1/customer/rate/{id:\\d+}", method = RequestMethod.POST)
//    public Rating ratingFoodStall(@PathVariable("id") Long foodStallId) {
//
//
//        return  ratingService.
//    }
}
