package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserTopicDTO {
    private Long id;
    private String userId;
    private Long topicId;
    private boolean completed;
    private boolean unlocked;
    private List<UserModuleDTO> unlockedModules;
    private List<UserModuleDTO> completedModules;
}
