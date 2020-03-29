package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.User;

@Service
public interface IUserService {

    User saveUser(User user);

    User getUserInfo(String username);

    String getUserRole(String username, String password);

    User getUserInformation(Long userId);

    User loginWithUsernameAndPwd(String username, String password);
}
