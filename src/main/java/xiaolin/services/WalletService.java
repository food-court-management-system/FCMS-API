package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.IWalletRepository;
import xiaolin.entities.Wallet;

@Service
public class WalletService implements IWalletService{

    @Autowired
    IWalletRepository walletRepository;

    @Override
    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getCustomerWalletByWalletId(Long walletId, Boolean status) {
        return walletRepository.getCustomerWallet(walletId, status);
    }

    @Override
    public Wallet searchCustomerWalletByCustomerId(Long customerId) {
        return walletRepository.findWalletByCustomerId(customerId);
    }
}
