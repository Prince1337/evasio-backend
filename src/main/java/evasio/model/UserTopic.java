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
public class UserTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    @JsonIgnore
    private Topic topic;

    private boolean completed;
    private boolean unlocked;
    
    @OneToMany(mappedBy = "userTopic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<UserModules> userModules = new ArrayList<>();
    
    public List<UserModules> getUserModules() {
        return userModules;
    }

    // Helper-Methoden für bidirektionale Beziehung
    public void addUserModule(UserModules userModule) {
        userModules.add(userModule);
        userModule.setUserTopic(this);
    }

    public void removeUserModule(UserModules userModule) {
        userModules.remove(userModule);
        userModule.setUserTopic(null);
    }
    
    // Convenience-Methoden für Frontend
    public List<UserModules> getUnlockedModules() {
        return userModules.stream()
                .filter(UserModules::isUnlocked)
                .toList();
    }
    
    public List<UserModules> getCompletedModules() {
        return userModules.stream()
                .filter(UserModules::isCompleted)
                .toList();
    }
}
