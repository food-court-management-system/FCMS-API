package xiaolin.dtos.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDetailDTO implements Serializable {

    private Long userId;
    private String lastName;
    private String firstName;
    private int age;
    private String username;
}
