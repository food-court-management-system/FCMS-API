package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.FoodStall;
import xiaolin.entities.User;

@Getter
@Setter
public class UserDto {

    private Long userId;
    private String username;
    private String password;
    private String fName;
    private String lName;
    private String role;
    private int age;
    private Long foodStallID;
    private boolean isActive;
    private String token;

    public UserDto() {}

}
