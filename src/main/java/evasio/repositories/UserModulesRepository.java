package evasio.repositories;

import evasio.model.UserModules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "userModules", collectionResourceRel = "userModules")
public interface UserModulesRepository extends JpaRepository<UserModules, Long> {
    UserModules findByUserIdAndModuleId(String userId, Long moduleId);

    List<UserModules> findByUserIdAndUserTopicId(String userId, Long id);
}
