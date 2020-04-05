package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.IUserRepository;
import xiaolin.dtos.UserDto;
import xiaolin.dtos.mapper.FCMSMapper;
import xiaolin.dtos.user.UpdateProfileDTO;
import xiaolin.entities.User;
import xiaolin.util.FCMSUtil;

import java.util.List;

@Service
public class UserService implements IUserService{

    @Autowired
    IUserRepository userRepository;


    @Override
    public User saveUser(User user) {
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

    @Override
    public User getUserInformation(Long userId) {
        return userRepository.getUserInformation(userId);
    }

    @Override
    public User getUserInformation(String username) {
        return userRepository.getUserInformation(username);
    }

    @Override
    public User loginWithUsernameAndPwd(String username, String password) {
        return userRepository.loginWithUsernameAndPwd(username, password);
    }

    @Override
    public List<User> getAllUserOfFoodCourtBaseOnRole(String role) {
        return userRepository.getAllUserOfFoodCourtBaseOnRole(role);
    }

    @Override
    public List<User> getAllFoodStallStaffOfFoodStall(Long foodStallId) {
        return userRepository.getAllFoodStallStaffOfFoodStall(foodStallId);
    }

    @Override
    public void updateProfile(UpdateProfileDTO updateProfileDTO) {
        User user = userRepository.findByUserName(updateProfileDTO.getUsername());
        user.setFirstName(updateProfileDTO.getFirstName());
        user.setLastName(updateProfileDTO.getLastName());
        user.setAge(updateProfileDTO.getAge());
        userRepository.save(user);
    }
}
