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
    public Wallet createWalletForNewCustomer(Wallet wallet) {
        Wallet w = walletRepository.save(wallet);
        System.out.println(w.getBalances());
        System.out.println(w.getId());
        System.out.println(w.isActive());
        System.out.println(w.getInUseBalances());
        return w;
    }
}
