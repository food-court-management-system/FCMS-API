package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Customer;

@Service
public interface ICustomerService {

    Customer checkExistCustomer(String provider, String email);

    Customer createNewCustomer(Customer customer);
}
