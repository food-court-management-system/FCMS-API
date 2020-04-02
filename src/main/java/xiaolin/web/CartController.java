package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.CartDto;
import xiaolin.dtos.CartItemRes;
import xiaolin.entities.CartItem;
import xiaolin.services.ICartService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    @ResponseBody
    @RequestMapping(value = "/history/{walletId:\\d+}", method = RequestMethod.GET)
    public ResponseEntity<Object> getHistoryOrder(@PathVariable("walletId") Long walletId) {
        return new ResponseEntity<>(cartService.getHistoryOrder(walletId),HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/detail/{cartId:\\d+}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOrderDetail(@PathVariable("cartId") Long cartId) {
        List<CartItem> find = cartService.getOrderDetail(cartId);
        List<CartItemRes> result = new ArrayList<>();
        for (CartItem cartItem: find) {
            CartItemRes cartItemRes = new CartItemRes();
            cartItemRes.setFoodId(cartItem.getFoodId().getId());
            cartItemRes.setFoodName(cartItem.getFoodId().getFoodName());
            cartItemRes.setFoodStallId(cartItem.getFoodId().getFoodStall().getFoodStallId());
            cartItemRes.setFoodStallName(cartItem.getFoodId().getFoodStall().getFoodStallName());
            cartItemRes.setId(cartItem.getId());
            cartItemRes.setPurchasedPrice(cartItem.getPurchasedPrice());
            cartItemRes.setQuantity(cartItem.getQuantity());
            cartItemRes.setNote(cartItem.getNote());
            result.add(cartItemRes);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity order(@RequestBody CartDto cart) {
        cartService.order(cart);
        return new ResponseEntity(HttpStatus.OK);
    }
}
