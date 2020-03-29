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
            user.setFName("Administrator");
            userService.insertUser(user);
        }
    }
}
