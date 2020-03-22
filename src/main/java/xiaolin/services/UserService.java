package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.IUserRepository;
import xiaolin.dtos.UserDto;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.entities.User;
import xiaolin.util.FCMSUtil;

@Service
public class UserService implements IUserService{

    @Autowired
    IUserRepository userRepository;


    @Override
    public User insertUser(User user) {

//        String username = "admin";
//        String password = "123456789";
//        String role = "admin";
//        boolean isActive = true;
//
//        FCMSUtil util = new FCMSUtil();
//        String encodePassword = util.encodePassword(password);
//        UserDto dto = new UserDto();
//        dto.setUsername(username);
//        dto.setPassword(encodePassword);
//        dto.setRole(role);
//        dto.setActive(isActive);
//        User user = FCMSMapper.mapToUser(dto);
////        User user = new User(username, encodePassword, role, isActive);
//        userRepository.save(user);
//        user.setUserName("cashier_1");
//        user.setRole("cashier");
//        userRepository.save(user);
//        user.setUserName("cashier_2");
//        userRepository.save(user);
//        user.setUserName("food_stall_1");
//        user.setRole("foodstall");
//        userRepository.save(user);
//        user.setUserName("food_stall_2");
//        userRepository.save(user);
//        user.setUserName("food_stall_3");
        return userRepository.save(user);
    }

    @Override
    public User getUserInfo(String username) {
        User user = userRepository.getUserInfo(username);
        return user;
    }

    @Override
    public String getUserRole(String username, String password) {
        return userRepository.getUserRole(username, password);
    }
}
