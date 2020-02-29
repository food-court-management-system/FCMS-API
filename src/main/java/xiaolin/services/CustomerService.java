package xiaolin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaolin.dao.ICustomerRepository;

@Service
public class CustomerService implements ICustomerService{

    @Autowired
    ICustomerRepository customerRepository;
}
