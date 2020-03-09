package xiaolin.config.jwt;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xiaolin.dao.IUserRepository;
import xiaolin.util.FCMSUtil;

import java.util.ArrayList;

@Service
public class FCMSUserDetailService implements UserDetailsService {

    @Autowired
    IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = userRepository.getUserPassword(username);
        if (password == null) {
            int indexOfAt = username.indexOf("@");
            String user = username.substring(0, indexOfAt);
            System.out.println("User:  " + user);
            String id = username.substring(indexOfAt + 1);
            System.out.println("ID: " + id);
            return new User(user, FCMSUtil.encodePassword(id), new ArrayList<>());
        }
        return new User(username, password, new ArrayList<>());
    }
}
