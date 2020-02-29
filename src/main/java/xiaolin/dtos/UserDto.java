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

}
