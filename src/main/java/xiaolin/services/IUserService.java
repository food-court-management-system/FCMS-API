package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.User;

@Service
public interface IUserService {

    User insertUser(User user);

    User getUserInfo(String username);

    String getUserRole(String username, String password);
}
