package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import xiaolin.services.ICustomerService;

@RestController
public class CustomerController {

    @Autowired
    ICustomerService customerService;
}
