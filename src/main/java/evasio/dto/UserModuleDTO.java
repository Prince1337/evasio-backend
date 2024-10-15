package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class UserModuleDTO {
    private Long id;
    private String userId;
    private Long moduleId;
    private boolean completed;
    private boolean unlocked;
}