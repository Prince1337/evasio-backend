package evasio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "user_topic_id")
    @JsonIgnore
    private UserTopic userTopic;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonIgnore
    private Module module;

    private boolean completed;
    private boolean unlocked;
}