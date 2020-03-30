package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.User;

import java.util.List;

@Service
public interface IUserService {

    User saveUser(User user);

    User getUserInfo(String username);

    String getUserRole(String username, String password);

    User getUserInformation(Long userId);

    User loginWithUsernameAndPwd(String username, String password);

    List<User> getAllUserOfFoodCourtBaseOnRole(String role);
}
