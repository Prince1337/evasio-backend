package evasio.api;

import evasio.dto.ModuleDTO;
import evasio.model.Module;
import evasio.services.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modules")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:4200")
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public ResponseEntity<List<ModuleDTO>> findAll() {
        List<ModuleDTO> modules = moduleService.findAll();
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<ModuleDTO>> findAllByTopicId(@PathVariable Long topicId) { // Use @PathVariable instead of @RequestParam
        List<ModuleDTO> modules = moduleService.findAllByTopicId(topicId);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleDTO> findById(@PathVariable Long id) {
        ModuleDTO module = moduleService.findById(id);
        if (module != null) {
            return new ResponseEntity<>(module, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ModuleDTO> add(@Valid @RequestBody ModuleDTO moduleDto) {
        ModuleDTO savedModule = moduleService.add(moduleDto);
        return new ResponseEntity<>(savedModule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleDTO> update(@PathVariable Long id, @RequestBody ModuleDTO moduleDto) {
        System.out.println("ModuleDto: " + moduleDto);
        ModuleDTO updatedModule = moduleService.update(moduleDto);
        if (updatedModule != null) {
            return new ResponseEntity<>(updatedModule, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        moduleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
