package tr.com.cemalaydin.dashboard.modules.user;

import tr.com.cemalaydin.dashboard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id <> :userId and  (lower(u.fullName) like :searchText or lower(u.email) like :searchText or lower(u.username) like :searchText)")
    List<User> findBySearch(String userId ,String searchText);
}
