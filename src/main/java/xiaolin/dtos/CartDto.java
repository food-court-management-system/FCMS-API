package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class CartDto {

    private Long walletId;
    private List<CartItemDto> cartItems;

}
