package evasio.api;

import evasio.dto.TopicDTO;
import evasio.services.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:4200")
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicDTO>> findAll() {
        List<TopicDTO> topics = topicService.findAll();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> findById(@PathVariable Long id) {
        TopicDTO topic = topicService.findById(id);
        if (topic != null) {
            return new ResponseEntity<>(topic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<TopicDTO> add(@Valid @RequestBody TopicDTO topicDto) {
        TopicDTO savedTopic = topicService.add(topicDto);
        return new ResponseEntity<>(savedTopic, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDTO> update(@PathVariable Long id, @Valid @RequestBody TopicDTO topicDto) {
        System.out.println("TopicDto: " + topicDto);
        topicDto.setTopicId(id);
        TopicDTO updatedTopic = topicService.update(topicDto);
        if (updatedTopic != null) {
            System.out.println("UpdatedTopic: " + updatedTopic);
            return new ResponseEntity<>(updatedTopic, HttpStatus.OK);
        } else {
            System.out.println("updatedTopic: " + updatedTopic);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        topicService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TopicDTO>> findByTitle(@RequestParam String title, @RequestParam(required = false) String description, @RequestParam(required = false) String category) {
        List<TopicDTO> topics = topicService.searchByTitle(title, description, category);
        if (!topics.isEmpty()) {
            return new ResponseEntity<>(topics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
