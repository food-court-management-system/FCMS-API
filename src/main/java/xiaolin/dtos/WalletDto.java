package xiaolin.dtos;

import lombok.Getter;
import lombok.Setter;
import xiaolin.entities.Wallet;

@Getter
@Setter
public class WalletDto {

    private Long walletId;
    private long balance;
    private long inUseBalance;
    private boolean isActive;

    public WalletDto() {
    }

}
