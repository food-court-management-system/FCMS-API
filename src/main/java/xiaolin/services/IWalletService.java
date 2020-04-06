package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Wallet;

@Service
public interface IWalletService {

    Wallet saveWallet(Wallet wallet);

    Wallet getCustomerWalletByWalletId(Long walletId, Boolean status);

    Wallet searchCustomerWalletByCustomerId(Long customerId);

    Wallet findById(Long walletId);
}
