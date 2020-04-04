package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.ICustomerRepository;
import xiaolin.entities.Customer;

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


}
