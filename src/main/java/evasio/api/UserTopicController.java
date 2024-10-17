package evasio.api;

import evasio.dto.UserModuleDTO;
import evasio.dto.UserTopicDTO;
import evasio.model.Module;
import evasio.model.Topic;
import evasio.services.ModuleService;
import evasio.services.UserTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userTopics")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:4200")
public class UserTopicController {

    private final UserTopicService userTopicService;
    private final ModuleService moduleService;

    @GetMapping
    public ResponseEntity<List<UserTopicDTO>> findAll() {
        List<UserTopicDTO> userTopics = userTopicService.findAll();
        return new ResponseEntity<>(userTopics, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/topic/{topicId}")
    public ResponseEntity<UserTopicDTO> findByUserIdAndTopicId(@PathVariable String userId, @PathVariable Long topicId) {
        UserTopicDTO userTopic = userTopicService.findByUserIdAndTopicId(userId, topicId);
        if (userTopic != null) {
            return new ResponseEntity<>(userTopic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/modules/{userId}/{topicId}")
    public ResponseEntity<List<UserModuleDTO>> getUserModulesByUserIdAndTopicId(@PathVariable String userId, @PathVariable Long topicId) {
        List<UserModuleDTO> userModules = userTopicService.getUserModulesByUserIdAndTopicId(userId, topicId);
        return new ResponseEntity<>(userModules, HttpStatus.OK);
    }

    @PostMapping("/modules-by-ids")
    public ResponseEntity<List<Module>> getModulesByIds(@RequestBody List<Long> moduleIds) {
        List<Module> modules = moduleService.getModulesByIds(moduleIds);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserTopicDTO>> findAllByUserId(@PathVariable String userId) {
        List<UserTopicDTO> userTopics = userTopicService.findAllByUserId(userId);
        return new ResponseEntity<>(userTopics, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTopicDTO> findById(@PathVariable Long id) {
        UserTopicDTO userTopic = userTopicService.findById(id);
        if (userTopic != null) {
            return new ResponseEntity<>(userTopic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<UserTopicDTO> add(@RequestBody UserTopicDTO userTopicDTO) {
        UserTopicDTO savedUserTopic = userTopicService.add(userTopicDTO);
        return new ResponseEntity<>(savedUserTopic, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserTopicDTO> update(@PathVariable Long id, @RequestBody UserTopicDTO userTopicDTO) {
        userTopicDTO.setId(id);
        UserTopicDTO updatedUserTopic = userTopicService.update(userTopicDTO);
        if (updatedUserTopic != null) {
            return new ResponseEntity<>(updatedUserTopic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userTopicService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/available/{userId}")
    public ResponseEntity<List<Topic>> findAllAvailable(@PathVariable String userId) {
        List<Topic> availableUserTopics = userTopicService.findAllAvailable(userId);
        return new ResponseEntity<>(availableUserTopics, HttpStatus.OK);
    }

    @GetMapping("/completed/{userId}")
    public ResponseEntity<List<Topic>> findAllCompleted(@PathVariable String userId) {
        List<Topic> completedUserTopics = userTopicService.findAllCompleted(userId);
        return new ResponseEntity<>(completedUserTopics, HttpStatus.OK);
    }

    @GetMapping("/unlocked/{userId}")
    public ResponseEntity<List<Topic>> findAllUnlocked(@PathVariable String userId) {
        List<Topic> unlockedUserTopics = userTopicService.findAllUnlocked(userId);
        return new ResponseEntity<>(unlockedUserTopics, HttpStatus.OK);
    }

    @GetMapping("/not-unlocked/{userId}")
    public ResponseEntity<List<Topic>> findAllNotUnlocked(@PathVariable String userId) {
        List<Topic> notUnlockedUserTopics = userTopicService.findAllNotUnlocked(userId);
        return new ResponseEntity<>(notUnlockedUserTopics, HttpStatus.OK);
    }

    @PostMapping("/unlock-topic/{userId}/{topicId}")
    public ResponseEntity<Void> unlockTopic(@PathVariable String userId, @PathVariable Long topicId) {
        userTopicService.unlockTopic(userId, topicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/complete-module/{userId}/{topicId}/{moduleId}")
    public ResponseEntity<Void> completeModule(@PathVariable String userId, @PathVariable Long topicId, @PathVariable Long moduleId) {
        System.out.println("Complete module: " + userId + " " + topicId + " " + moduleId);
        userTopicService.completeModule(userId, moduleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
