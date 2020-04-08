package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CurrentOrderDto implements Serializable {

    private Long foodStallId;
    private String foodStallName;
    private List<CartItemRes> cartItems;
}
