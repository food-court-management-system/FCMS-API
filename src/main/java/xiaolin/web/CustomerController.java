package xiaolin.web;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.CustomerDto;
import xiaolin.entities.Customer;
import xiaolin.entities.Food;
import xiaolin.entities.Rating;
import xiaolin.entities.Wallet;
import xiaolin.services.ICustomerService;
import xiaolin.services.IFoodService;
import xiaolin.services.IRatingService;
import xiaolin.services.IWalletService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @Autowired
    IWalletService walletService;

    @Autowired
    IRatingService ratingService;

    @Autowired
    IFoodService foodService;

    @RequestMapping(value = "/wallet/{id:\\d+}/edit", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Object> updateBalanceCustomerWallet(@PathVariable("id") Long walletId,
                                                              @RequestParam(value = "balance", defaultValue = "0") String balance,
                                                              @RequestParam("assert") String userAssert,
                                                              @RequestParam(value = "status", defaultValue = "TRUE", required = false) Boolean status) {
        JsonObject jsonObject = new JsonObject();
        if (balance.equals("0")) {
            jsonObject.addProperty("message", "Balance didn't change because you don't input new balance");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } else {
            try {
                Float.parseFloat(balance);
            } catch (Exception e) {
                jsonObject.addProperty("message", "Balance must be a number");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            }
        }
        if (userAssert == null) {
            jsonObject.addProperty("message", "Customer assert cannot be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else {
            if (userAssert.equalsIgnoreCase("Deposit")) {
                Wallet customerWallet = walletService.getCustomerWalletByWalletId(walletId, status);
                if (customerWallet != null) {
                    float newBalance = customerWallet.getBalances() + Float.parseFloat(balance);
                    customerWallet.setBalances(newBalance);
                    Wallet result = walletService.saveWallet(customerWallet);
                    if (result != null) {
                        return new ResponseEntity<>(result, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
            }
            if (userAssert.equalsIgnoreCase("Withdrawals")) {
                Wallet customerWallet = walletService.getCustomerWalletByWalletId(walletId, status);
                if (customerWallet != null) {
                    if (customerWallet.getBalances() < Float.parseFloat(balance)) {
                        jsonObject.addProperty("message", "Customer wallet don't have enough balance");
                        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
                    }
                    float newBalance = customerWallet.getBalances() - Float.parseFloat(balance);
                    customerWallet.setBalances(newBalance);
                    Wallet result = walletService.saveWallet(customerWallet);
                    if (result != null) {
                        return new ResponseEntity<>(result, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
            }
            jsonObject.addProperty("message", "Customer assert must be deposit or withdrawals");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id:\\d+}/wallet/detail", method = RequestMethod.GET)
    public ResponseEntity<Object> searchCustomerWalletByCustomerId(@PathVariable("id") Long customerId) {
        JsonObject jsonObject = new JsonObject();
        Wallet customerWallet = walletService.searchCustomerWalletByCustomerId(customerId);
        if (customerWallet == null) {
            jsonObject.addProperty("message", "Cannot find that customer wallet");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(customerWallet, HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/wallet/{id:\\d+}", method = RequestMethod.PUT)
    public ResponseEntity<Object> deactiveOrActiveCustomerWallet(@PathVariable("id") Long walletId,
                                                                 @RequestParam(value = "status", defaultValue = "TRUE", required = false) Boolean status) {
        JsonObject jsonObject = new JsonObject();
        Wallet customerWallet = walletService.getCustomerWalletByWalletId(walletId, status);
        if (customerWallet == null) {
            jsonObject.addProperty("message", "Cannot find that customer wallet");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else {
            if (status.booleanValue()) {
                customerWallet.setActive(false);
            } else {
                customerWallet.setActive(true);
            }
            Wallet result = walletService.saveWallet(customerWallet);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                jsonObject.addProperty("message", "Deactive customer wallet failed");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/google/lists", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCustomerHasProviderGoogle() {
        List<Customer> googleCustomer = customerService.getAllCustomerBaseOnProvider("Google");
        List<CustomerDto> result = new ArrayList<>();
        if (googleCustomer != null) {
            for(Customer customer : googleCustomer) {
                CustomerDto customerDto = new CustomerDto();
                customerDto.setId(customer.getId());
                customerDto.setActive(customer.isActive());
                customerDto.setEmail(customer.getEmail());
                customerDto.setProvider(customer.getProvider());
                customerDto.setWalletId(customer.getWallet().getId());
                result.add(customerDto);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/facebook/lists", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCustomerHasProviderFacebook() {
        List<Customer> facebookCustomer = customerService.getAllCustomerBaseOnProvider("Facebook");
        List<CustomerDto> result = new ArrayList<>();
        if (facebookCustomer != null) {
            for(Customer customer : facebookCustomer) {
                CustomerDto customerDto = new CustomerDto();
                customerDto.setId(customer.getId());
                customerDto.setActive(customer.isActive());
                customerDto.setEmail(customer.getEmail());
                customerDto.setProvider(customer.getProvider());
                customerDto.setWalletId(customer.getWallet().getId());
                result.add(customerDto);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/lists/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCustomerAccountInFoodCourt(){
        List<Customer> googleCustomer = customerService.getAllCustomerBaseOnProvider("Google");
        List<Customer> facebookCustomer = customerService.getAllCustomerBaseOnProvider("Facebook");
        List<CustomerDto> result = new ArrayList<>();
        if (facebookCustomer != null) {
            for(Customer customer : facebookCustomer) {
                CustomerDto customerDto = new CustomerDto();
                customerDto.setId(customer.getId());
                customerDto.setActive(customer.isActive());
                customerDto.setEmail(customer.getEmail());
                customerDto.setProvider(customer.getProvider());
                customerDto.setWalletId(customer.getWallet().getId());
                result.add(customerDto);
            }
        }
        if (googleCustomer != null) {
            for(Customer customer : googleCustomer) {
                CustomerDto customerDto = new CustomerDto();
                customerDto.setId(customer.getId());
                customerDto.setActive(customer.isActive());
                customerDto.setEmail(customer.getEmail());
                customerDto.setProvider(customer.getProvider());
                customerDto.setWalletId(customer.getWallet().getId());
                result.add(customerDto);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/customer/{id:\\d+}/rate/{food-id:\\d+}", method = RequestMethod.POST)
    public ResponseEntity<Object> ratingFoodStall(@PathVariable("food-id") Long foodId,
                                                  @PathVariable("id") Long customerId,
                                                  @RequestParam("star") Float ratingStar) {
        JsonObject jsonObject = new JsonObject();
        if(ratingStar == null) {
            jsonObject.addProperty("message", "Rating must not be empty");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        Rating rating = new Rating();
        rating.setRatingStar(ratingStar);
        rating.setRatingDate(LocalDate.now());
        Customer customer = customerService.getCustomerById(customerId);
        rating.setCustomer(customer);
        Food food = foodService.getFoodDetailById(foodId);
        rating.setFood(food);
        float foodNewRating = (food.getFoodRating() + ratingStar) / 2;
        food.setFoodRating(foodNewRating);
        foodService.saveFood(food);
        Rating result = ratingService.logRating(rating);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
