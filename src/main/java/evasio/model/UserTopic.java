package evasio.model;

import evasio.dto.UserModuleDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

    private String userId;

    private Long topicId;

    private boolean completed;
    private boolean unlocked;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserModules> unlockedModules;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserModules> completedModules;
}
