package evasio.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long topicId;

    private String title;
    @Column(length = 65535)
    private String description;
    private String category;
    private String difficulty;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Module> modules;

    public void setModules(List<Module> newModules) {
        if(this.modules != null) {
            this.modules.clear();
            if (newModules != null) {
                this.modules.addAll(newModules);
            }
        } else {
            this.modules = newModules;
        }

    }


}
