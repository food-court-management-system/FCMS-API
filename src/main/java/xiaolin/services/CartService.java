package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.ICartItemRepository;
import xiaolin.dao.ICartRepository;
import xiaolin.dao.IFoodRepository;
import xiaolin.dao.IWalletRepository;
import xiaolin.dtos.CartDto;
import xiaolin.dtos.CartItemDto;
import xiaolin.dtos.CartItemRes;
import xiaolin.entities.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService{

    @Autowired
    ICartRepository cartRepository;

    @Autowired
    ICartItemRepository cartItemRepository;

    @Autowired
    IWalletRepository walletRepository;

    @Autowired
    IFoodRepository foodRepository;


    @Override
    public List<Cart> getHistoryOrder(Long id) {
        return cartRepository.getAllByWallet_Id(id);
    }

    @Override
    public boolean order(CartDto cartDto) {
        Wallet wallet = walletRepository.findById(cartDto.getWalletId()).get();
        if (wallet == null || !wallet.isActive() || wallet.getBalances() == 0) {
            return false;
        }

        List<CartItemDto> sorted = cartDto.getCartItems();
        Collections.sort(sorted);
        cartDto.setCartItems(sorted);

        List<Long> ids  = cartDto.getCartItems().stream()
                            .map(CartItemDto::getFoodId)
                            .collect(Collectors.toList());
        List<Food> foods = foodRepository.findByIdIn(ids);


        Cart cart = new Cart();
        cart.setCartStatus(Status.PENDING);
        cart.setCheckOut(true);
        cart.setPurchaseDate(LocalDate.now());
        cart.setWallet(wallet);

        List<CartItem> cartItems = new ArrayList<>();
        float sum = 0;

        for (int i = 0; i < foods.size(); i++) {
            sum += foods.get(i).getRetailPrice() * sorted.get(i).getQuantity();
            if (sum > wallet.getBalances()) {
                return false;
            }
            CartItem cartItem = new CartItem();
            cartItem.setFoodId(foods.get(i));
            cartItem.setFoodStatus(FoodStatus.QUEUE);
            cartItem.setNote(sorted.get(i).getNote());
            cartItem.setQuantity(sorted.get(i).getQuantity());
            cartItem.setPurchasedPrice(foods.get(i).getRetailPrice() * sorted.get(i).getQuantity());
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }

        cart.setTotalPrice(sum);
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
        wallet.setBalances(wallet.getBalances() - sum);
        walletRepository.save(wallet);
        return true;
    }

    @Override
    public List<CartItem> getOrderDetail(Long cartId) {
        return cartItemRepository.findAllByCart_Id(cartId);
    }

    @Override
    public List<CartItemRes> getAllCartItemInProcess(Long foodStallId) {
        List<Food> foods = foodRepository.findAllByFoodStall_FoodStallIdAndIsActive(foodStallId, true);
        List<Long> ids = foods.stream()
                .map(Food::getId)
                .collect(Collectors.toList());

        List<CartItem> cartItems = cartItemRepository.findAll().stream()
                .filter(o -> ids.contains(o.getFoodId().getId()) && !o.getFoodStatus().equals(FoodStatus.DELIVERY) && !o.getFoodStatus().equals(FoodStatus.CANCLE))
                .collect(Collectors.toList());

        List<CartItemRes> result = new ArrayList<>();
        for (CartItem cartItem: cartItems) {
            CartItemRes r = new CartItemRes();
            r.setNote(cartItem.getNote());
            r.setQuantity(cartItem.getQuantity());
            r.setPurchasedPrice(cartItem.getPurchasedPrice());
            r.setFoodName(cartItem.getFoodId().getFoodName());
            r.setId(cartItem.getId());
            r.setFoodStatus(cartItem.getFoodStatus().toString());
            result.add(r);
        }
        return result;
    }

    @Override
    public CartItemRes getCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        CartItemRes result = new CartItemRes();
        result.setNote(cartItem.getNote());
        result.setQuantity(cartItem.getQuantity());
        result.setPurchasedPrice(cartItem.getPurchasedPrice());
        result.setFoodName(cartItem.getFoodId().getFoodName());
        result.setId(cartItem.getId());
        result.setFoodStatus(cartItem.getFoodStatus().toString());
        return result;
    }


}
