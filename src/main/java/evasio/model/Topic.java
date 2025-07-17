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
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    private String title;
    @Column(length = 65535)
    private String description;
    private String category;
    private String difficulty;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Module> modules = new ArrayList<>();

    // Helper-Methoden für bidirektionale Beziehung
    public void addModule(Module module) {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        modules.add(module);
        module.setTopic(this);
    }

    public void removeModule(Module module) {
        if (modules != null) {
            modules.remove(module);
            module.setTopic(null);
        }
    }

    public void setModules(List<Module> newModules) {
        if (this.modules != null) {
            this.modules.clear();
            if (newModules != null) {
                for (Module module : newModules) {
                    addModule(module);
                }
            }
        } else {
            this.modules = newModules;
        }
    }
}
