package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto implements Comparable<CartItemDto> {

    private Long foodId;
    private int quantity;
    private String note;

    @Override
    public int compareTo(CartItemDto cartItemDto) {
        return (foodId < cartItemDto.getFoodId() ? -1 :
                (foodId == cartItemDto.getFoodId() ? 0 : 1));
    }
}
