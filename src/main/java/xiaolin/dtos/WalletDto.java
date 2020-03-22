package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Wallet;

@Getter
@Setter
public class WalletDto {

    private Long walletId;
    private float balance;
    private float inUseBalance;
    private boolean isActive;

    public WalletDto() {
    }

}
