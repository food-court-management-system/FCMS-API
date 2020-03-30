package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import xiaolin.entities.User;

import java.util.List;


@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM tbl_users u WHERE u.username = :username", nativeQuery = true)
    User getUserInfo(@Param("username") String username);

    @Query(value = "SELECT u.role FROM tbl_users u WHERE u.username = :username " +
                                "AND u.password = :password " +
                                "AND u.is_active = 'TRUE'", nativeQuery = true)
    String getUserRole(@Param("username") String username,
                       @Param("password") String password);

    @Query(value = "SELECT u.* FROM tbl_users u WHERE u.role = :role AND u.is_active = 'TRUE'", nativeQuery = true)
    List<User> getAllUserOfFoodCourtBaseOnRole(@Param("role") String role);

    @Query(value = "SELECT u.password FROM tbl_users u WHERE u.username = :username AND u.is_active = 'TRUE'", nativeQuery = true)
    String getUserPassword(@Param("username") String username);

    @Query(value = "SELECT u.* FROM tbl_users u WHERE u.id = :id AND u.is_active = 'TRUE'", nativeQuery = true)
    User getUserInformation(@Param("id") Long userId);

    @Query(value = "SELECT u.* FROM tbl_users u WHERE u.username = :username " +
                                "AND u.password = :password " +
                                "AND u.is_active = 'TRUE'", nativeQuery = true)
    User loginWithUsernameAndPwd(@Param("username") String username,
                                 @Param("password") String password);
}
