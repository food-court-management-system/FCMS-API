package xiaolin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.CartDto;
import xiaolin.dtos.CartItemDto;
import xiaolin.dtos.CartItemRes;
import xiaolin.dtos.CartResultDTO;
import xiaolin.entities.*;
import xiaolin.services.ICartService;
import xiaolin.services.ICustomerService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    @Autowired
    ICustomerService customerService;

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

    @ResponseBody
    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCartItemInProcess(@RequestParam("foodStallId")Long foodStallId) {
        return new ResponseEntity<>(cartService.getAllCartItemInProcess(foodStallId),HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseEntity<Object> getCartItem(@RequestParam("cartItemId")Long cartItemId) {
        return new ResponseEntity<>(cartService.getCartItem(cartItemId),HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateStatusOrderDetail(@RequestParam("cartItemId")Long cartItemId, @RequestParam("status") FoodStatus foodStatus) {
        cartService.updateStatusOrderDetail(cartItemId, foodStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/{walletId:\\d+}/history", method = RequestMethod.GET)
    public ResponseEntity<Object> getCustomerHistoryOrder(@PathVariable("walletId") Long walletId) {
        List<Cart> result = new ArrayList<>();
        List<Cart> listAllHistory = cartService.getHistoryOrder(walletId);
        for(Cart c: listAllHistory) {
            if (c.getCartStatus() == Status.CANCEL) {
                result.add(c);
            }
            if (c.getCartStatus() == Status.DONE) {
                result.add(c);
            }
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/{id:\\d+}/in-progress/detail", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCartInProgressForCustomer(@PathVariable("id") Long walletId) {
//        Customer customer = customerService.getCustomerById(customerId);
        List<Cart> listAllInProgressCart = cartService.getHistoryOrder(walletId);
        List<CartResultDTO> result = new ArrayList<>();
        for(Cart c: listAllInProgressCart) {
            if (c.getCartStatus() == Status.INPROGRESS || c.getCartStatus() == Status.PENDING) {
                List<CartItem> find = cartService.getOrderDetail(c.getId());
                List<CartItemRes> cartItemResList = new ArrayList<>();
                for (CartItem cartItem : find) {
                    CartItemRes cartItemRes = new CartItemRes();
                    cartItemRes.setFoodId(cartItem.getFoodId().getId());
                    cartItemRes.setFoodName(cartItem.getFoodId().getFoodName());
                    cartItemRes.setFoodStallId(cartItem.getFoodId().getFoodStall().getFoodStallId());
                    cartItemRes.setFoodStallName(cartItem.getFoodId().getFoodStall().getFoodStallName());
                    cartItemRes.setId(cartItem.getId());
                    cartItemRes.setPurchasedPrice(cartItem.getPurchasedPrice());
                    cartItemRes.setQuantity(cartItem.getQuantity());
                    cartItemRes.setNote(cartItem.getNote());
                    cartItemRes.setFoodStatus(cartItem.getFoodStatus().toString());
                    cartItemResList.add(cartItemRes);
                }
                CartResultDTO cartResultDTO = new CartResultDTO();
                cartResultDTO.setCartStatus(c.getCartStatus());
                cartResultDTO.setId(c.getId());
                cartResultDTO.setTotalPrice(c.getTotalPrice());
                cartResultDTO.setCartItems(cartItemResList);
                result.add(cartResultDTO);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
