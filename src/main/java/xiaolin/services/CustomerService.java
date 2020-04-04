package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.ICustomerRepository;
import xiaolin.entities.Customer;

import java.util.List;

@Service
public class CustomerService implements ICustomerService{

    @Autowired
    ICustomerRepository customerRepository;

    @Override
    public Customer checkExistCustomer(String provider, String email) {
        return customerRepository.checkExistCustomer(provider, email);
    }

    @Override
    public Customer createNewCustomer(Customer customer) {
        Customer cus = customerRepository.save(customer);
        System.out.println(cus.getId());
        System.out.println(cus.isActive());
        System.out.println(cus.getProvider());
        System.out.println(cus.getWallet().getId());
        System.out.println(cus.getProvider());
        return cus;
    }

    @Override
    public List<Customer> getAllCustomerBaseOnProvider(String provider) {
        return customerRepository.getAllCustomerBaseOnProvider(provider);
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.getCustomerDetailById(customerId);
    }

    @Override
    public Customer checkCustomerActiveOrDeactive(Long customerId, Boolean status) {
        return customerRepository.checkCustomerActiveOrDeactive(customerId, status);
    }
}
