package xiaolin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import xiaolin.config.jwt.FCMSUserDetailService;
import xiaolin.config.jwt.JwtUtil;
import xiaolin.dtos.LoginFormDto;
import xiaolin.dtos.UserDto;
import xiaolin.dtos.user.ChangePasswordDTO;
import xiaolin.dtos.user.UserDetailDTO;
import xiaolin.entities.Customer;
import xiaolin.entities.FoodStall;
import xiaolin.entities.User;
import xiaolin.entities.Wallet;
import xiaolin.services.ICustomerService;
import xiaolin.services.IFoodStallService;
import xiaolin.services.IUserService;
import xiaolin.services.IWalletService;
import xiaolin.util.FCMSUtil;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    Environment environment;

    @Autowired
    IUserService userService;

    @Autowired
    ICustomerService customerService;

    @Autowired
    IWalletService walletService;

    @Autowired
    FCMSUserDetailService fcmsUserDetailService;

    @Autowired
    IFoodStallService foodStallService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/customer/social-account", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> loginSocialAccount(@RequestParam("accessToken") String accessToken,
                                                     @RequestParam("provider") String provider) {
        String link, email;
        Wallet result;
        if (provider.equals("Google")) {
            link = String.format(environment.getProperty("google.link.get.profile"), accessToken);
        } else {
            link = String.format(environment.getProperty("facebook.link.get.profile"), accessToken);
        }
        System.out.println("link: " + link);
        try {
            String response = Request.Get(link).execute().returnContent().asString();
            ObjectMapper mapper = new ObjectMapper();
            email = mapper.readTree(response).get("email").textValue();
            System.out.println("email: " + email);
            Customer cus = customerService.checkExistCustomer(provider, email);
            if (cus == null) {
                Wallet newWallet = new Wallet();
                newWallet.setActive(true);
                newWallet.setInUseBalances(0);
                newWallet.setBalances(0);
                Wallet wallet = walletService.saveWallet(newWallet);
                result = wallet;
                if (wallet != null) {
                    cus = new Customer();
                    cus.setWallet(newWallet);
                    cus.setProvider(provider);
                    cus.setActive(true);
                    cus.setEmail(email);
                    customerService.createNewCustomer(cus);
                }
            } else {
                result = walletService.searchCustomerWalletByCustomerId(cus.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id:\\d+}/detail"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getUserDetail(@PathVariable("id") Long userId){
        JsonObject jsonObject = new JsonObject();
        User user = userService.getUserInformation(userId);
        if (user != null) {
            UserDetailDTO result = new UserDetailDTO();
            result.setUserId(user.getId());
            result.setUsername(user.getUserName());
            result.setFirstName(user.getFirstName());
            result.setLastName(user.getLastName());
            result.setAge(user.getAge());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            jsonObject.addProperty("message", "Cannot found that user with that id");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> loginWithUsernameAndPwd(@RequestBody LoginFormDto dto) {
        JsonObject jsonObject = new JsonObject();
        if (dto.getUsername().length() == 0) {
            jsonObject.addProperty("message", "Username is null. Please insert username");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
        if (dto.getPassword().length() == 0) {
            jsonObject.addProperty("message", "Password is null. Please insert password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
        String username = dto.getUsername();
        String password = dto.getPassword();
        FCMSUtil fcmsUtil = new FCMSUtil();
        String encodedPwd = fcmsUtil.encodePassword(password);
        User user = userService.loginWithUsernameAndPwd(username, encodedPwd);
        if (user == null) {
            jsonObject.addProperty("message", "Wrong username and password. Please login again");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(jsonObject.toString());
        }

        if (user.getFoodStall() != null && !user.getFoodStall().getIsActive()) {
            jsonObject.addProperty("message", "Your foodStall is disabled");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(jsonObject.toString());
        }

        UserDetails userDetails = fcmsUserDetailService.loadUserByUsername(username);

        String jwt  = jwtUtil.generateToken(userDetails, user.getRole());
        UserDto result = new UserDto();
        result.setUserId(user.getId());
        result.setUsername(user.getUserName());
        result.setPassword(null);
        result.setFName(user.getFirstName());
        result.setLName(user.getLastName());
        result.setAge(user.getAge());
        result.setRole(user.getRole());
        result.setActive(true);
        result.setFoodStallId(user.getFoodStall() != null ? user.getFoodStall() .getFoodStallId() : null);
        result.setToken(jwt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:\\d+}/change-password", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Object> changeUserPassword(@PathVariable("id") Long userId,
                                                     @RequestBody ChangePasswordDTO changePasswordDTO) {
        User user = userService.getUserInformation(userId);
        JsonObject jsonObject = new JsonObject();
        if (changePasswordDTO.getNewPassword() == null) {
            jsonObject.addProperty("message", "Must have new password for changing");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (changePasswordDTO.getOldPassword() == null) {
            jsonObject.addProperty("message", "Must have new password for changing");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
        if (user != null) {
            String encodedOldPwd = FCMSUtil.encodePassword(changePasswordDTO.getOldPassword());
            if (!user.getPassword().equals(encodedOldPwd)) {
                jsonObject.addProperty("message", "Old password is not match please check again");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            }
            user.setPassword(FCMSUtil.encodePassword(changePasswordDTO.getNewPassword()));
            userService.saveUser(user);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sad", method = RequestMethod.GET)
    public String testAccessToken() {
        return "This is access token works";
    }

}
