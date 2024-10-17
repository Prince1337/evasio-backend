package evasio.api;

import evasio.dto.QuizDTO;
import evasio.services.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quizzes")
@CrossOrigin(originPatterns = "http://localhost:4200")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<List<QuizDTO>> findAll() {
        List<QuizDTO> quizzes = quizService.findAll();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<QuizDTO>> findAllByModuleId(@PathVariable Long moduleId) {
        List<QuizDTO> quizzes = quizService.findAllByModuleId(moduleId);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> findById(@PathVariable Long id) {
        QuizDTO quiz = quizService.findById(id);
        if (quiz != null) {
            return new ResponseEntity<>(quiz, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<QuizDTO> add( @RequestBody QuizDTO quizDto) {
        QuizDTO savedQuiz = quizService.add(quizDto);
        return new ResponseEntity<>(savedQuiz, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> update(@PathVariable Long id, @RequestBody QuizDTO quizDto) {
        quizDto.setId(id);
        QuizDTO updatedQuiz = quizService.update(quizDto);
        if (updatedQuiz != null) {
            return new ResponseEntity<>(updatedQuiz, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quizService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
