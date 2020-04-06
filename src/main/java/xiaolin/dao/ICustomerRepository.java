package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Customer;

import java.util.List;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    //public List<Customer> getAllCustomers();

    @Query(value = "SELECT c FROM Customer c WHERE c.email = :email AND c.provider = :provider")
    Customer checkExistCustomer(@Param("provider") String provider,
                                @Param("email") String email);

    @Query(value = "SELECT c FROM Customer c WHERE c.provider = :provider")
    List<Customer> getAllCustomerBaseOnProvider(@Param("provider") String provider);

    @Query(value = "SELECT c.* FROM tbl_customers c WHERE c.id = :id AND c.is_active = 'TRUE'", nativeQuery = true)
    Customer getCustomerDetailById(@Param("id") Long customerId);

    @Query(value = "SELECT c.* FROM tbl_customers c WHERE c.id = :id AND c.is_active = :status", nativeQuery = true)
    Customer checkCustomerActiveOrDeactive(@Param("id") Long customerId,
                                           @Param("status") Boolean status);
}
