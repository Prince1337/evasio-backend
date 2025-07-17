package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTopicDTO {
    private Long id;
    private String userId;
    private Long topicId;
    private boolean completed;
    private boolean unlocked;
    private List<UserModuleDTO> unlockedModules;
    private List<UserModuleDTO> completedModules;
}
