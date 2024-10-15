package evasio.services;

import evasio.dto.UserModuleDTO;
import evasio.dto.UserTopicDTO;
import evasio.model.*;
import evasio.model.Module;
import evasio.repositories.ModuleRepository;
import evasio.repositories.TopicRepository;
import evasio.repositories.UserModulesRepository;
import evasio.repositories.UserTopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTopicService {

    private final UserTopicRepository userTopicRepository;
    private final UserModulesRepository userModulesRepository;
    private final TopicRepository topicRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public List<UserTopicDTO> findAll() {
        List<UserTopic> userTopics = userTopicRepository.findAll();
        return userTopics.stream()
                .map(this::convertUserTopicToDTO)
                .toList();
    }

    @Transactional
    public UserTopicDTO findById(Long id) {
        Optional<UserTopic> userTopicsOpt = userTopicRepository.findById(id);
        return userTopicsOpt.map(this::convertUserTopicToDTO).orElse(null);
    }

    @Transactional
    public UserTopicDTO add(UserTopicDTO userTopicDTO) {
        UserTopic userTopic = convertUserTopicDTOToEntity(userTopicDTO);
        UserTopic savedUserTopic = userTopicRepository.save(userTopic);
        return convertUserTopicToDTO(savedUserTopic);
    }

    @Transactional
    public UserTopicDTO update(UserTopicDTO userTopicDTO) {
        Optional<UserTopic> userTopicsOpt = userTopicRepository.findById(userTopicDTO.getId());
        if (userTopicsOpt.isPresent()) {
            UserTopic userTopic = convertUserTopicDTOToEntity(userTopicDTO);
            UserTopic updatedUserTopic = userTopicRepository.save(userTopic);
            return convertUserTopicToDTO(updatedUserTopic);
        } else {
            throw new ResourceNotFoundException("UserTopic not found with id " + userTopicDTO.getId());
        }
    }

    @Transactional
    public void delete(Long id) {
        userTopicRepository.deleteById(id);
    }

    @Transactional
    public List<Topic> findAllAvailable(String userId) {
        List<Topic> topics = topicRepository.findAll();
        // get all topics that are not user topics
        List<Topic> filteredTopics = topics.stream()
                .filter(topic -> !userTopicRepository.existsByUserIdAndTopicId(userId, topic.getTopicId()))
                .toList();

        return filteredTopics;
    }

    @Transactional
    public List<Topic> findAllCompleted(String userId) {
        List<UserTopic> userTopics = userTopicRepository.findAllByUserId(userId);
        List<UserTopic> filteredUserTopics = userTopics.stream()
                .filter(UserTopic::isCompleted)
                .filter(UserTopic::isUnlocked)
                .toList();

        return filteredUserTopics.stream()
                .map(userTopic -> topicRepository.findById(userTopic.getTopicId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public List<Topic> findAllUnlocked(String userId) {
        List<UserTopic> userTopics = userTopicRepository.findAllByUserId(userId);
        List<UserTopic> filteredUserTopics = userTopics.stream()
                .filter(UserTopic::isUnlocked)
                .filter(userTopic -> !userTopic.isCompleted())
                .toList();

        return filteredUserTopics.stream()
                .map(userTopic -> topicRepository.findById(userTopic.getTopicId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public List<Topic> findAllNotUnlocked(String userId) {
        List<UserTopic> userTopics = userTopicRepository.findAllByUserId(userId);
        List<UserTopic> filteredUserTopics = userTopics.stream()
                .filter(userTopic -> !userTopic.isUnlocked())
                .toList();

        return filteredUserTopics.stream()
                .map(userTopic -> topicRepository.findById(userTopic.getTopicId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public void unlockTopic(String userId, Long topicId) {
        if (userId == null || topicId == null) {
            throw new IllegalArgumentException("userId and topicId cannot be null");
        }
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));

        // if topic is already unlocked, return
        if (userTopicRepository.existsByUserIdAndTopicId(userId, topic.getTopicId())) {
            return;
        }

        UserTopic userTopic = UserTopic.builder()
                .userId(userId)
                .topicId(topic.getTopicId())
                .completed(false)
                .unlocked(true)
                .unlockedModules(new ArrayList<>())
                .completedModules(new ArrayList<>())
                .build();
        UserModules userModules = UserModules.builder()
                .userId(userId)
                .userTopic(userTopic)
                .module(topic.getModules().get(0))
                .unlocked(true)
                .completed(false)
                .build();
        userTopic.getUnlockedModules().add(userModules);
        userTopic.setCompletedModules(new ArrayList<>());

        userTopicRepository.save(userTopic);
    }

    @Transactional
    public void unlockModule(String userId, Long topicId, Long moduleId) {
        // Find the existing UserTopic
        UserTopic userTopic = userTopicRepository.findByUserIdAndTopicIdOrderByIdAsc(userId, topicId);

        // Find the module to unlock
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        // Check if the module is already unlocked
        boolean isAlreadyUnlocked = userTopic.getUnlockedModules().stream()
                .anyMatch(um -> um.getModule().getId().equals(moduleId));

        if (!isAlreadyUnlocked) {
            // Create a new UserModule entity
            UserModules userModule = UserModules.builder()
                    .userId(userId)
                    .userTopic(userTopic)
                    .module(module)
                    .unlocked(true)
                    .completed(false)
                    .build();

            // Add the new UserModule to the unlockedModules list
            userTopic.getUnlockedModules().add(userModule);

            // Save the updated UserTopic entity
            userTopicRepository.save(userTopic);
            System.out.println("Module unlocked successfully");
        }
    }

    @Transactional
    private void unlockNextModule(String userId, Long currentModuleId) {
        System.out.println("unlockNextModule called with userId: " + userId + ", currentModuleId: " + currentModuleId);
        Module currentModule = moduleRepository.findById(currentModuleId).orElseThrow(() -> new RuntimeException("Module not found"));
        Topic topic = currentModule.getTopic();
        List<Module> modules = moduleRepository.findByTopicTopicId(topic.getTopicId());

        int currentIndex = modules.indexOf(currentModule);
        if (currentIndex < modules.size() - 1) {
            System.out.println("unlocking next module");
            System.out.println("current index: " + currentIndex);
            System.out.println("modules size: " + modules.size());
            System.out.println("next index: " + (currentIndex + 1));
            Module nextModule = modules.get(currentIndex + 1);
            unlockModule(userId, topic.getTopicId(), nextModule.getId());
        }
    }

    @Transactional
    private void unlockNextTopic(String userId, Long currentTopicId) {
        Topic currentTopic = topicRepository.findById(currentTopicId).orElseThrow(() -> new RuntimeException("Topic not found"));
        List<Topic> topics = topicRepository.findAll();

        int currentIndex = topics.indexOf(currentTopic);
        if (currentIndex < topics.size() - 1) {
            Topic nextTopic = topics.get(currentIndex + 1);
            unlockTopic(userId, nextTopic.getTopicId());
        }
    }

    @Transactional
    public UserTopicDTO findByUserIdAndTopicId(String userId, Long topicId) {
        UserTopic userTopic = userTopicRepository.findByUserIdAndTopicIdOrderByIdAsc(userId, topicId);
        List<UserModuleDTO> unlockedModulesDTO = userTopic.getUnlockedModules().stream()
                .map(this::mapUserModuleToUserModuleDTO)
                .toList();
        List<UserModuleDTO> completedModulesDTO = userTopic.getCompletedModules().stream()
                .map(this::mapUserModuleToUserModuleDTO)
                .toList();

        return UserTopicDTO.builder()
                .userId(userTopic.getUserId())
                .topicId(userTopic.getTopicId())
                .completed(userTopic.isCompleted())
                .unlocked(userTopic.isUnlocked())
                .unlockedModules(unlockedModulesDTO)
                .completedModules(completedModulesDTO)
                .build();
    }

    public List<UserModuleDTO> getUserModulesByUserIdAndTopicId(String userId, Long topicId) {
        // Annahme: UserModuleRepository hat eine Methode, um UserModules nach Benutzer-ID und Topic-ID zu suchen
        List<UserModules> userModules = userModulesRepository.findByUserIdAndUserTopicId(userId, topicId);
        return userModules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserModuleDTO convertToDTO(UserModules userModules) {
        return UserModuleDTO.builder()
                .userId(userModules.getUserId())
                .moduleId(userModules.getModule().getId())
                .unlocked(userModules.isUnlocked())
                .completed(userModules.isCompleted())
                .build();
    }

    private UserModuleDTO mapUserModuleToUserModuleDTO(UserModules userModules) {
        return UserModuleDTO.builder()
                .userId(userModules.getUserId())
                .moduleId(userModules.getModule().getId())
                .unlocked(userModules.isUnlocked())
                .completed(userModules.isCompleted())
                .build();
    }

    @Transactional
    public void completeModule(String userId, Long moduleId) {
        UserModules userModule = userModulesRepository.findByUserIdAndModuleId(userId, moduleId);
        userModule.setCompleted(true);
        userModulesRepository.save(userModule);

        UserTopic userTopic = userTopicRepository.findByUserIdAndTopicIdOrderByIdAsc(userId, userModule.getUserTopic().getTopicId());
        userTopic.getCompletedModules().add(userModule);
        userTopicRepository.save(userTopic);

        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> new RuntimeException("Module not found"));
        Topic topic = module.getTopic();
        List<UserModules> userModules = userModulesRepository.findByUserIdAndUserTopicId(userId, topic.getTopicId());

        System.out.println("user topic size : " + userTopic.getCompletedModules().size());
        System.out.println("topic modules size: " + topic.getModules().size());

        if(userTopic.getCompletedModules().size() >= topic.getModules().size()) {
            System.out.println("all modules completed");
            completeTopic(userId, topic.getTopicId());
        } else {
            System.out.println("not all modules completed, unlocking next module");
            unlockNextModule(userId, moduleId);
        }

    }

    @Transactional
    private void completeTopic(String userId, Long topicId) {
        UserTopic userTopic = userTopicRepository.findByUserIdAndTopicIdOrderByIdAsc(userId, topicId);
        userTopic.setCompleted(true);
        userTopicRepository.save(userTopic);
        System.out.println("Topic completed: " + userTopic.getTopicId());
        //unlockNextTopic(userId, topicId);
    }

    private UserModuleDTO convertUserModulesToDTO(UserModules userModule) {
        return UserModuleDTO.builder()
                .id(userModule.getId())
                .userId(userModule.getUserId())
                .moduleId(userModule.getModule().getId())
                .completed(userModule.isCompleted())
                .unlocked(userModule.isUnlocked())
                .build();
    }

    private UserModules convertUserModuleDTOToUserModules(UserModuleDTO userModuleDTO) {
        UserModules userModule = new UserModules();
        userModule.setId(userModuleDTO.getId());
        userModule.setUserId(userModuleDTO.getUserId());
        userModule.setModule(moduleRepository.findById(userModuleDTO.getModuleId()).orElseThrow(() -> new RuntimeException("Module not found")));
        userModule.setCompleted(userModuleDTO.isCompleted());
        userModule.setUnlocked(userModuleDTO.isUnlocked());
        return userModule;
    }

    private UserTopicDTO convertUserTopicToDTO(UserTopic userTopic) {
        return UserTopicDTO.builder()
                .id(userTopic.getId())
                .userId(userTopic.getUserId())
                .topicId(userTopic.getTopicId())
                .completed(userTopic.isCompleted())
                .unlocked(userTopic.isUnlocked())
                .unlockedModules(userTopic.getUnlockedModules().stream().map(this::convertUserModulesToDTO).toList())
                .completedModules(userTopic.getCompletedModules().stream().map(this::convertUserModulesToDTO).toList())
                .build();
    }

    private UserTopic convertUserTopicDTOToEntity(UserTopicDTO userTopicDTO) {
        UserTopic userTopic = new UserTopic();
        userTopic.setId(userTopicDTO.getId());
        userTopic.setUserId(userTopicDTO.getUserId());
        userTopic.setTopicId(userTopicDTO.getTopicId());
        userTopic.setCompleted(userTopicDTO.isCompleted());
        userTopic.setUnlocked(userTopicDTO.isUnlocked());
        userTopic.setUnlockedModules(userTopicDTO.getUnlockedModules().stream().map(this::convertUserModuleDTOToUserModules).toList());
        userTopic.setCompletedModules(userTopicDTO.getCompletedModules().stream().map(this::convertUserModuleDTOToUserModules).toList());
        return userTopic;
    }

    public List<UserTopicDTO> findAllByUserId(String userId) {
        List<UserTopic> userTopics = userTopicRepository.findAllByUserId(userId);
        return userTopics.stream().map(this::convertUserTopicToDTO).toList();
    }
}
