package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Wallet;

@Service
public interface IWalletService {

    Wallet createWalletForNewCustomer(Wallet wallet);
}
