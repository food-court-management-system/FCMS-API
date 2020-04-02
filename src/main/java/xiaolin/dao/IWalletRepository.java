package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Wallet;

@Repository
public interface IWalletRepository extends JpaRepository<Wallet, Long> {
    @Query(value = "SELECT w.* FROM tbl_wallets w, tbl_customers c WHERE w.id = c.wallet_id AND c.id = :cus_id", nativeQuery = true)
    Wallet findWalletByCustomerId(@Param("cus_id") Long customerId);

    @Query(value = "SELECT w.* FROM tbl_wallets w WHERE w.id = :wallet_id AND w.is_active = :status", nativeQuery = true)
    Wallet getCustomerWallet(@Param("wallet_id") Long walletId,
                             @Param("status") Boolean walletStatus);
}
