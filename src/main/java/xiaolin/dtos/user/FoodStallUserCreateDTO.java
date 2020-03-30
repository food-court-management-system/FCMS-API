package xiaolin.dtos.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FoodStallUserCreateDTO implements Serializable {

    private String lastName;
    private String firstName;
    private int age;
    private String username;
    private String password;
    private Long foodStallId;
}
