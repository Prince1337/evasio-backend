package evasio.services;

import evasio.dto.QuizDTO;
import evasio.model.Module;
import evasio.model.Quiz;
import evasio.repositories.ModuleRepository;
import evasio.repositories.QuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public List<QuizDTO> findAll() {
        List<Quiz> quizzes = quizRepository.findAll();
        return quizzes.stream().map(this::convertToDto).toList();
    }

    @Transactional
    public List<QuizDTO> findAllByModuleId(Long moduleId) {
        List<Quiz> quizzes = quizRepository.findAllByModuleId(moduleId);
        return quizzes.stream().map(this::convertToDto).toList();
    }

    @Transactional
    public QuizDTO findById(Long id) {
        Optional<Quiz> quizOpt = quizRepository.findById(id);
        return quizOpt.map(this::convertToDto).orElse(null);
    }

    @Transactional
    public QuizDTO add(QuizDTO quizDto) {
        Quiz quiz = convertToEntity(quizDto);
        Module module = moduleRepository.findModuleById(quizDto.getModuleId());
        module.addQuiz(quiz);
        moduleRepository.save(module);
        return convertToDto(quiz);
    }

    @Transactional
    public QuizDTO update(QuizDTO quizDto) {
        Quiz oldQuiz = quizRepository.findQuizById(quizDto.getId());
        if (oldQuiz != null) {
            oldQuiz.setId(quizDto.getId());
            oldQuiz.setQuestion(quizDto.getQuestion());
            oldQuiz.setOptions(quizDto.getOptions());
            oldQuiz.setQuestionType(quizDto.getQuestionType());
            oldQuiz.setMatchingOptions(quizDto.getMatchingOptions());
            oldQuiz.setCorrectAnswer(quizDto.getCorrectAnswer());
            
            // Update module relationship if changed
            Module newModule = moduleRepository.findModuleById(quizDto.getModuleId());
            if (!newModule.getId().equals(oldQuiz.getModule().getId())) {
                oldQuiz.getModule().removeQuiz(oldQuiz);
                newModule.addQuiz(oldQuiz);
                moduleRepository.save(newModule);
            }
            
            return convertToDto(quizRepository.save(oldQuiz));
        } else {
            throw new ResourceNotFoundException("Quiz not found with id " + quizDto.getId());
        }
    }

    @Transactional
    public void delete(Long id) {
        quizRepository.deleteById(id);
    }

    public QuizDTO convertToDto(Quiz quiz) {
        return QuizDTO.builder()
                .id(quiz.getId())
                .questionType(quiz.getQuestionType())
                .matchingOptions(quiz.getMatchingOptions())
                .question(quiz.getQuestion())
                .options(quiz.getOptions())
                .correctAnswer(quiz.getCorrectAnswer())
                .moduleId(quiz.getModule().getId())
                .build();
    }

    public Quiz convertToEntity(QuizDTO quizDto) {
        return Quiz.builder()
                .id(quizDto.getId())
                .question(quizDto.getQuestion())
                .options(quizDto.getOptions())
                .questionType(quizDto.getQuestionType())
                .matchingOptions(quizDto.getMatchingOptions())
                .correctAnswer(quizDto.getCorrectAnswer())
                .module(moduleRepository.findModuleById(quizDto.getModuleId()))
                .build();
    }
}
