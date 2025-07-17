package evasio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ElementCollection
    private List<String> options;

    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ElementCollection
    private List<String> matchingOptions;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    @JsonIgnore
    private Module module;
}
