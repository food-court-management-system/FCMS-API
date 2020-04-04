package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomerStatusDTO implements Serializable {
    private Long customerId;
    private Boolean status;
}
