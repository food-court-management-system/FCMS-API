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

    public Wallet mapToWallet(WalletDto walletDto) {
        Wallet result = new Wallet();
        if (walletDto.getWalletId() != null) {
            result.setId(walletDto.getWalletId());
        }
        result.setBalances(walletDto.getBalance());
        result.setInUseBalances(walletDto.getInUseBalance());
        result.setActive(walletDto.isActive());
        return result;
    }
}
