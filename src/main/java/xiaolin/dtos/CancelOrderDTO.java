package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CancelOrderDTO implements Serializable {

    private Long id;
    private String reason;
}
