package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginFormDto implements Serializable {
    private String username;
    private String password;

    public LoginFormDto() {
    }
}
