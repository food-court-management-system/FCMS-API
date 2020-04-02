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
import xiaolin.entities.Customer;
import xiaolin.entities.User;
import xiaolin.entities.Wallet;
import xiaolin.services.ICustomerService;
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
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/customer/social-account", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> loginSocialAccount(@RequestParam("accessToken") String accessToken,
                                                     @RequestParam("provider") String provider) {
        String link, email;
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
                Wallet wallet = walletService.createWalletForNewCustomer(newWallet);
                if (wallet != null) {
                    cus = new Customer();
                    cus.setWallet(newWallet);
                    cus.setProvider(provider);
                    cus.setActive(true);
                    cus.setEmail(email);
                    customerService.createNewCustomer(cus);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = {"/detail"}, method = RequestMethod.GET)
    @ResponseBody
    public User getUserDetail(@RequestParam("username") String username){
        return userService.getUserInfo(username);
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
        result.setToken(jwt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
