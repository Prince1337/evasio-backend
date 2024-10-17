package evasio.repositories;

import evasio.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByTopicId(Long topicId);

    Optional<Topic> findByTitle(String topicTitle);

    List<Topic> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String description, String category);
}
