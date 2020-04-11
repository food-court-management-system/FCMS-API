package xiaolin.web;

import org.hibernate.mapping.Collection;
import org.omg.CORBA.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xiaolin.dtos.*;
import xiaolin.entities.*;
import xiaolin.services.ICartService;
import xiaolin.services.ICustomerService;
import xiaolin.services.IFoodStallService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    @Autowired
    ICustomerService customerService;

    @Autowired
    IFoodStallService foodStallService;

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
            if (cartItem.getFoodStatus().equals(FoodStatus.CANCEL)) {
                cartItemRes.setReason(cartItem.getReason());
            }
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
        List<Cart> listAllInProgressCart = cartService.getHistoryOrder(walletId);
        List<CurrentOrderDto> result = new ArrayList<>();
        for(Cart c: listAllInProgressCart) {
            if (c.getCartStatus() == Status.INPROGRESS || c.getCartStatus() == Status.PENDING) {
                List<CartItem> find = cartService.getOrderDetail(c.getId());
                for (CartItem ci: find) {
                    CurrentOrderDto currentOrderDto = new CurrentOrderDto();
                    currentOrderDto.setFoodStallId(ci.getFoodId().getFoodStall().getFoodStallId());
                    currentOrderDto.setFoodStallName(ci.getFoodId().getFoodStall().getFoodStallName());
                    currentOrderDto.setCartItems(new ArrayList<>());
                    result.add(currentOrderDto);
                }
            }
        }
        List<Long> ids = result.stream().map(CurrentOrderDto::getFoodStallId).distinct().collect(Collectors.toList());
        result = new ArrayList<>();
        for (Long id: ids) {
            FoodStall foodStall = foodStallService.getFoodStallDetail(id.longValue());
            CurrentOrderDto currentOrderDto = new CurrentOrderDto();
            currentOrderDto.setFoodStallId(foodStall.getFoodStallId());
            currentOrderDto.setFoodStallName(foodStall.getFoodStallName());
            currentOrderDto.setCartItems(new ArrayList<>());
            result.add(currentOrderDto);
        }
        for(Cart c: listAllInProgressCart) {
            if (c.getCartStatus() == Status.INPROGRESS || c.getCartStatus() == Status.PENDING) {
                List<CartItem> find = cartService.getOrderDetail(c.getId());
                for (CartItem ci: find) {
                    for (CurrentOrderDto cod: result) {
                        if (ci.getFoodId().getFoodStall().getFoodStallId().longValue() == cod.getFoodStallId().longValue()) {
                            CartItemRes cartItemRes = new CartItemRes();
                            cartItemRes.setFoodStatus(ci.getFoodStatus().toString());
                            cartItemRes.setFoodName(ci.getFoodId().getFoodName());
                            cartItemRes.setId(ci.getId());
                            cartItemRes.setNote(ci.getNote());
                            cartItemRes.setQuantity(ci.getQuantity());
                            cartItemRes.setPurchasedPrice(ci.getPurchasedPrice());
                            if (ci.getFoodStatus().equals(FoodStatus.CANCEL)) {
                                cartItemRes.setReason(ci.getReason());
                            }
                            cod.getCartItems().add(cartItemRes);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.PUT)
    public ResponseEntity<Object> cancelOrder(@RequestBody CancelOrderDTO cancelOrderDTO) {
        cartService.cancelOrder(cancelOrderDTO.getId(), cancelOrderDTO.getReason());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
