package xiaolin.services;

import org.springframework.stereotype.Service;
import xiaolin.entities.Customer;

import java.util.List;

@Service
public interface ICustomerService {

    Customer checkExistCustomer(String provider, String email);

    Customer createNewCustomer(Customer customer);

    List<Customer> getAllCustomerBaseOnProvider(String provider);

    Customer getCustomerById(Long customerId);

    Customer checkCustomerActiveOrDeactive(Long customerId, Boolean status);
}
