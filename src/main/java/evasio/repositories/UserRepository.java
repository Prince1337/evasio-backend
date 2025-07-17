package evasio.repositories;

import evasio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

    User findByEmail(String customerEmail);
}
