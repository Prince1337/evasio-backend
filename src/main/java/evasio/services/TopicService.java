package evasio.services;

import evasio.dto.ModuleDTO;
import evasio.dto.TopicDTO;
import evasio.dto.QuizDTO;
import evasio.model.Module;
import evasio.model.Quiz;
import evasio.model.Topic;
import evasio.repositories.ModuleRepository;
import evasio.repositories.QuizRepository;
import evasio.repositories.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    @Transactional
    public List<TopicDTO> findAll() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream().map(this::convertToDto).toList();
    }

    @Transactional
    public TopicDTO findById(Long id) {
        Optional<Topic> topicOpt = topicRepository.findById(id);
        return topicOpt.map(this::convertToDto).orElse(null);
    }

    @Transactional
    public TopicDTO findByTitle(String title) {
        Optional<Topic> topicOpt = topicRepository.findByTitle(title);
        return topicOpt.map(this::convertToDto).orElse(null);
    }

    @Transactional
    public TopicDTO add(TopicDTO topicDto) {
        Topic topic = convertTopicDtoToEntity(topicDto);
        Topic savedTopic = topicRepository.save(topic);
        return convertToDto(savedTopic);
    }

    @Transactional
    public TopicDTO update(TopicDTO topicDto) {
        Optional<Topic> topicOpt = topicRepository.findById(topicDto.getTopicId());
        if (topicOpt.isPresent()) {
            System.out.println("Topic found with id " + topicDto.getTopicId());
            Topic existingTopic = topicOpt.get();
            // Update the existing topic with the values from the DTO
            existingTopic.setTitle(topicDto.getTitle());
            existingTopic.setCategory(topicDto.getCategory());
            existingTopic.setDifficulty(topicDto.getDifficulty());
            existingTopic.setDescription(topicDto.getDescription());
            // Update the modules
            if (topicDto.getModules() != null) {
                // convert the list of ModuleDTO to list of Module
                List<Module> modules = topicDto.getModules().stream().map((ModuleDTO moduleDto) -> convertModuleDtoToEntity(moduleDto, existingTopic)).collect(Collectors.toList());
                existingTopic.setModules(modules);
            }
            // Save the updated topic
            Topic updatedTopic = topicRepository.save(existingTopic);
            System.out.println("Topic updated with id " + topicDto.getTopicId());
            System.out.println("Category: " + topicDto.getCategory());
            return convertToDto(updatedTopic);
        } else {
            throw new ResourceNotFoundException("Topic not found with id " + topicDto.getTopicId());
        }
    }

    @Transactional
    public void delete(Long id) {
        topicRepository.deleteById(id);
    }

    private TopicDTO convertToDto(Topic topic) {
        TopicDTO topicDto = TopicDTO.builder()
                .topicId(topic.getTopicId())
                .title(topic.getTitle())
                .description(topic.getDescription()) // Set description
                .category(topic.getCategory()) // Set category
                .difficulty(topic.getDifficulty()) // Set difficulty
                .build();
        if (topic.getModules() != null) {
            List<ModuleDTO> moduleDtos = topic.getModules().stream().map(this::convertModuleToDto).toList();
            topicDto.setModules(moduleDtos);
        }
        return topicDto;
    }


    private ModuleDTO convertModuleToDto(Module module) {
        ModuleDTO moduleDto = ModuleDTO.builder()
                .id(module.getId())
                .title(module.getTitle())
                .content(module.getContent())
                .build();
        if (module.getQuizzes() != null) {
            List<QuizDTO> quizDtos = module.getQuizzes().stream().map(this::convertQuizToDto).collect(Collectors.toList());
            moduleDto.setQuizzes(quizDtos);
        }
        return moduleDto;
    }

    private QuizDTO convertQuizToDto(Quiz quiz) {
        return QuizDTO.builder()
                .id(quiz.getId())
                .question(quiz.getQuestion())
                .options(quiz.getOptions())
                .questionType(quiz.getQuestionType())
                .matchingOptions(quiz.getMatchingOptions())
                .correctAnswer(quiz.getCorrectAnswer())
                .build();

    }

    private Topic convertTopicDtoToEntity(TopicDTO topicDto) {
        Topic topic = Topic.builder()
                .title(topicDto.getTitle())
                .description(topicDto.getDescription()) // Set description
                .category(topicDto.getCategory()) // Set category
                .difficulty(topicDto.getDifficulty()) // Set difficulty
                .build();

        if (topicDto.getModules() != null) {
            List<Module> modules = topicDto.getModules().stream()
                    .map(dto -> convertModuleDtoToEntity(dto, topic))
                    .collect(Collectors.toList());
            topic.setModules(modules);
        }
        return topic;
    }



    private Module convertModuleDtoToEntity(ModuleDTO moduleDto, Topic topic) {
        Module module = Module.builder()
                .title(moduleDto.getTitle())
                .content(moduleDto.getContent())
                .topic(topic)
                .build();

        if (moduleDto.getQuizzes() != null) {
            List<Quiz> quizzes = moduleDto.getQuizzes().stream()
                    .map(quizDto -> convertQuizDtoToEntity(quizDto, module))
                    .toList();
            module.setQuizzes(quizzes);
        }
        return module;
    }



    private Quiz convertQuizDtoToEntity(QuizDTO quizDto, Module module) {
        return Quiz.builder()
                .question(quizDto.getQuestion())
                .options(quizDto.getOptions())
                .correctAnswer(quizDto.getCorrectAnswer())
                .questionType(quizDto.getQuestionType())
                .matchingOptions(quizDto.getMatchingOptions())
                .module(module)
                .build();
    }

    public List<TopicDTO> searchByTitle(String title, String description, String category) {
        List<Topic> topics = topicRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategoryContainingIgnoreCase(title, description, category);
        return topics.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }
}
