package xiaolin.dtos.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FoodStallManagerDetailDTO implements Serializable {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private int age;
    private Long fsid;
    private String fsname;
}
