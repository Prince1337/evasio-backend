package evasio.repositories;

import evasio.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByName(String admin);

    Set<Role> getRolesByNameIsIn(Set<String> roles);

    Role findByName(String role);

    Double getMaxContractValueByName(String role);
}
