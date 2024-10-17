package evasio.repositories;

import evasio.model.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {

    List<UserTopic> findAllByUserId(String userId);

    UserTopic findByUserIdAndTopicIdOrderByIdAsc(String userId, Long topicId);

    List<UserTopic> findByUserId(String userId);

    boolean existsByUserIdAndTopicId(String userId, Long topicId);
}