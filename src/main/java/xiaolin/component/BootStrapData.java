package xiaolin.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xiaolin.entities.User;
import xiaolin.services.IUserService;
import xiaolin.util.FCMSUtil;

@Component
public class BootStrapData implements CommandLineRunner {

    @Autowired
    IUserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userService.getUserInfo("admin") == null) {
            User user = new User();
            user.setUserName("admin");
            user.setPassword(FCMSUtil.encodePassword("admin"));
            user.setRole("admin");
            user.setActive(true);
            user.setFirstName("Administrator");
            userService.saveUser(user);
        }
        if (userService.getUserInfo("cashier") == null) {
            User user = new User();
            user.setUserName("cashier");
            user.setPassword(FCMSUtil.encodePassword("cashier"));
            user.setRole("cashier");
            user.setActive(true);
            user.setFirstName("Cashier");
            userService.saveUser(user);
        }
        if (userService.getUserInfo("manager") == null) {
            User user = new User();
            user.setUserName("manager");
            user.setPassword(FCMSUtil.encodePassword("manager"));
            user.setRole("manager");
            user.setActive(true);
            user.setFirstName("Food Stall Manager");
            userService.saveUser(user);
        }
    }
}
