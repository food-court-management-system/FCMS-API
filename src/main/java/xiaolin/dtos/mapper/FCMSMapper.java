package xiaolin.dtos.mapper;

import xiaolin.dtos.*;
import xiaolin.entities.*;

import java.util.ArrayList;

public class FCMSMapper {

    public static Cart mapToCart(CartDto cartDto) {
        Cart result = new Cart();
        if (cartDto.getId() != null) {
            result.setId(cartDto.getId());
        }
        result.setTotalPrice(cartDto.getTotalPrice());
        result.setCheckOut(cartDto.isCheckOut());
        if (cartDto.getCartStatus() != null) {
            result.setCartStatus(cartDto.getCartStatus());
        }
        if (cartDto.getPurchaseDate() != null) {
            result.setPurchaseDate(cartDto.getPurchaseDate());
        }
//        if (cartDto.getCustomerOwner() != null) {
//            result.setCustomerOwner(cartDto.getCustomerOwner());
//        }
        return result;
    }

    public static CartItem mapToCartItem(CartItemDto cartItemDto) {
        CartItem result = new CartItem();
        if (cartItemDto.getId() != null) {
            result.setId(cartItemDto.getId());
        }
        if (cartItemDto.getFoodId() != null) {
            result.setFoodId(cartItemDto.getFoodId());
        }
        result.setQuantity(cartItemDto.getQuantity());
        if (cartItemDto.getNote() != null) {
            result.setNote(cartItemDto.getNote());
        }
        if (cartItemDto.getFoodStatus() != null) {
            result.setFoodStatus(cartItemDto.getFoodStatus());
        }
//        if (cartItemDto.getCartOwner() != null) {
//            result.setCartOwner(cartItemDto.getCartOwner());
//        }
        result.setPurchasedPrice(cartItemDto.getPurchasePrice());
        return result;
    }

    public static Customer mapToCustomer(CustomerDto customerDto) {
        Customer result = new Customer();
        if (customerDto.getId() != null) {
            result.setId(customerDto.getId());
        }
        if (customerDto.getWallet() != null) {
            result.setWallet(customerDto.getWallet());
        }
        if (customerDto.getEmail() != null) {
            result.setEmail(customerDto.getEmail());
        }
        result.setActive(customerDto.isActive());
        if (customerDto.getProvider() != null) {
            result.setProvider(customerDto.getProvider());
        }
        return result;
    }

    public static FoodCourtInformation mapToFoodCourtInformation(FoodCourtInformationDto foodCourtInformationDto){
        FoodCourtInformation result = new FoodCourtInformation();
        if (foodCourtInformationDto.getFoodCourtId() != null){
            result.setFoodCourtId(foodCourtInformationDto.getFoodCourtId());
        }
        if (foodCourtInformationDto.getFoodCourtName() != null) {
            result.setFoodCourtName(foodCourtInformationDto.getFoodCourtName());
        }
        if (foodCourtInformationDto.getFoodCourtDescription() != null) {
            result.setFoodCourtDescription(foodCourtInformationDto.getFoodCourtDescription());
        }
        if (foodCourtInformationDto.getAddress() != null) {
            result.setFoodCourtAddress(foodCourtInformationDto.getAddress());
        }
        if (foodCourtInformationDto.getFoodCourtImage() != null) {
            result.setFoodCourtImage(foodCourtInformationDto.getFoodCourtImage());
        }
        return result;
    }

    public static Food mapToFood(FoodDto foodDto) {
        Food result = new Food();
        if (foodDto.getFoodId() != null) {
            result.setId(foodDto.getFoodId());
        }
        result.setOriginPrice(foodDto.getOriginPrice());
        if (foodDto.getFoodDescription() != null) {
            result.setFoodDescription(foodDto.getFoodDescription());
        }
        result.setRetailPrice(foodDto.getRetailPrice());
//        if (foodDto.getFoodType() != null) {
//            result.setFoodType(foodDto.getFoodType());
//        }
//        if (foodDto.getFoodStall() != null) {
//            result.setFoodStall(foodDto.getFoodStall());
//        }
        if (foodDto.getFoodImage() != null) {
            result.setFoodImage(foodDto.getFoodImage());
        }
        return result;
    }

    public static FoodStall mapToFoodStall(FoodStallDto foodStallDto) {
        FoodStall result = new FoodStall();
        if (foodStallDto.getFoodStallId() != null) {
            result.setFoodStallId(foodStallDto.getFoodStallId());
        }
        if (foodStallDto.getFoodStallName() != null) {
            result.setFoodStallName(foodStallDto.getFoodStallName());
        }
        if (foodStallDto.getFoodStallDescrption() != null) {
            result.setFoodStallDescription(foodStallDto.getFoodStallDescrption());
        }
        result.setFoodStallRating(foodStallDto.getFoodStallRating());
        if (foodStallDto.getFoodStallImage() != null) {
            result.setFoodStallImage(foodStallDto.getFoodStallImage());
        }
        return result;
    }

    public static Type mapToType(TypeDto typeDto) {
        Type result = new Type();
        if (typeDto.getTypeId() != null) {
            result.setId(typeDto.getTypeId());
        }
        if (typeDto.getTypeName() != null) {
            result.setTypeName(typeDto.getTypeName());
        }
        return result;
    }

    public static User mapToUser(UserDto userDto) {
        User result = new User();
        if (userDto.getUserId() != null) {
            result.setId(userDto.getUserId());
        }
        if (userDto.getFName() != null) {
            result.setFName(userDto.getFName());
        }
        if (userDto.getLName() != null) {
            result.setLName(userDto.getLName());
        }
        result.setAge(userDto.getAge());
//        if (userDto.getFoodStall() != null) {
//            result.setFoodStall(userDto.getFoodStall());
//        }
        if (userDto.getUsername() != null) {
            result.setUserName(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            result.setPassword(userDto.getPassword());
        }
        if (userDto.getRole() != null) {
            result.setRole(userDto.getRole());
        }
        result.setActive(userDto.isActive());
        return result;
    }

    public static Wallet mapToWallet(WalletDto walletDto) {
        Wallet result = new Wallet();
        if (walletDto.getWalletId() != null) {
            result.setId(walletDto.getWalletId());
        }
        result.setBalances(walletDto.getBalance());
        result.setInUseBalances(walletDto.getInUseBalance());
        result.setActive(walletDto.isActive());
        return result;
    }

    public static Rating mapToRating(RatingDto ratingDto) {
        Rating result = new Rating();
//        if (ratingDto.getCustomerRating() != null) {
//            result.setCustomer(ratingDto.getCustomerRating());
//        }
        result.setRatingStar(ratingDto.getRatingStar());
        if (ratingDto.getRatingDate() != null) {
            result.setRatingDate(ratingDto.getRatingDate());
        }
//        if (ratingDto.getFoodRated() != null) {
//            result.setFood(ratingDto.getFoodRated());
//        }
        return result;
    }


    public UserDto mapUserEntityToDto(User user) {
        UserDto result = null;

        return result;
    }
}
