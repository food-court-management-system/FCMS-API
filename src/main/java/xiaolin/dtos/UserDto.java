package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.FoodStall;
import xiaolin.entities.User;

@Getter
@Setter
public class UserDto {

    private Long userId;
    private String fName;
    private String lName;
    private int age;
    private FoodStall foodStall;
    private String username;
    private String password;
    private String role;
    private boolean isActive;

    public UserDto() {}

    public User mapToUser(UserDto userDto) {
        User result = new User();
        if (userDto.getUserId() != null) {
            result.setId(userDto.getUserId());
        }
        if (userDto.getFName() != null) {
            result.setFName(userDto.getFName());
        }
        if (userDto.getLName() != null) {
            result.setLName(userDto.getLName());
        }
        result.setAge(userDto.getAge());
        if (userDto.getFoodStall() != null) {
            result.setFoodStall(userDto.getFoodStall());
        }
        if (userDto.getUsername() != null) {
            result.setUserName(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            result.setPassword(userDto.getPassword());
        }
        if (userDto.getRole() != null) {
            result.setRole(userDto.getRole());
        }
        result.setActive(userDto.isActive());


        return result;
    }
}
