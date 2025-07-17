package evasio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 65535)
    private String content;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<UserModules> userModules = new ArrayList<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Quiz> quizzes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    @JsonIgnore
    private Topic topic;

    // Helper-Methoden für bidirektionale Beziehung mit Quiz
    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        quiz.setModule(this);
    }

    public void removeQuiz(Quiz quiz) {
        quizzes.remove(quiz);
        quiz.setModule(null);
    }

    public void setQuizzes(List<Quiz> newQuizzes) {
        if (this.quizzes != null) {
            this.quizzes.clear();
            if (newQuizzes != null) {
                for (Quiz quiz : newQuizzes) {
                    addQuiz(quiz);
                }
            }
        } else {
            this.quizzes = newQuizzes;
        }
    }

    public Module(Long moduleId) {
        this.id = moduleId;
    }
}
