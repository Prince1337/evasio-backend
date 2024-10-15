package evasio.repositories;

import evasio.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findAllByModuleId(Long moduleId);


    Quiz findQuizById(Long id);
}
