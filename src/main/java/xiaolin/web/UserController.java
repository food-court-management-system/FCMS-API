package xiaolin.web;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import xiaolin.config.jwt.FCMSUserDetailService;
import xiaolin.config.jwt.JwtUtil;
import xiaolin.entities.User;
import xiaolin.services.IUserService;
import xiaolin.util.FCMSUtil;

import java.util.Map;

@RestController
public class UserController {
    
    @Autowired
    IUserService userService;

    @Autowired
    FCMSUserDetailService fcmsUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = {"/sign-in"}, method = RequestMethod.POST)
    @ResponseBody
    public boolean User() {
        userService.insertUser();
        return true;
    }

    @RequestMapping(value = {"/detail"}, method = RequestMethod.GET)
    @ResponseBody
    public User getUserDetail(@RequestParam("username") String username){
        return userService.getUserInfo(username);
    }

    @RequestMapping(value = {"/api/v1/user/login"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> loginWithUsernameAndPwd(@RequestParam("username") String username,
                                                  @RequestParam("password") String password) throws Exception {
        String responseMsg;
        if (username.isEmpty()) {
            responseMsg = "Username cannot be null";
            return new ResponseEntity<>(responseMsg, HttpStatus.NOT_ACCEPTABLE);
        }
        if (password.isEmpty()) {
            responseMsg = "Password cannot be null";
            return new ResponseEntity<>(responseMsg, HttpStatus.NOT_ACCEPTABLE);
        }
        FCMSUtil fcmsUtil = new FCMSUtil();
        String encodedPwd = fcmsUtil.encodePassword(password);
        String role = userService.getUserRole(username, encodedPwd);
        if (role == null){
            responseMsg = "Wrong username and password. Please log in again";
            return new ResponseEntity<>(responseMsg, HttpStatus.NOT_ACCEPTABLE);
        }

        final UserDetails userDetails = fcmsUserDetailService.loadUserByUsername(username);

        final String jwt  = jwtUtil.generateToken(userDetails, role);

        Claims claims = jwtUtil.extractAllClaims(jwt);

        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

}
