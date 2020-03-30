package xiaolin.dtos.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePasswordDTO implements Serializable {

    private String newPassword;
    private String oldPassword;
}
