package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import evasio.model.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class QuizDTO {
    private Long id;

    @NotBlank(message = "Question cannot be blank")
    private String question;

    @NotNull(message = "Options cannot be null")
    @Size(min = 2, message = "At least two options are required")
    private List<String> options;

    @NotBlank(message = "Correct answer cannot be blank")
    private String correctAnswer;

    @NotBlank(message = "Question type cannot be blank")
    private QuestionType questionType;

    private List<String> matchingOptions;

    @NotNull(message = "Module cannot be null")
    private Long moduleId;

}