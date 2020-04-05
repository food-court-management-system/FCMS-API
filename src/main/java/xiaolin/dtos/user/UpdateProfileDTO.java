package xiaolin.dtos.user;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String lastName;
    private String firstName;
    private int age;
    private String username;
}
