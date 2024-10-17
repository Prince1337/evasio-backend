package evasio.repositories;

import evasio.dto.ModuleDTO;
import evasio.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findAllByTopicTopicId(Long topicId);
    List<Module> findByTopicTopicId(Long topicId);

    ModuleDTO findByTitleAndTopicTopicId(String moduleTitle, Long topicId);

    Module findModuleById(Long id);

    void deleteByTopicTopicId(Long id);

}
