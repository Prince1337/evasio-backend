package evasio.services;

import evasio.dto.ModuleDTO;
import evasio.dto.QuizDTO;
import evasio.model.Module;
import evasio.model.Quiz;
import evasio.model.Topic;
import evasio.repositories.ModuleRepository;
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
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final TopicRepository topicRepository;

    public List<ModuleDTO> findAll() {
        List<Module> modules = moduleRepository.findAll();
        return modules.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Module> getModulesByIds(List<Long> moduleIds) {
        return moduleRepository.findAllById(moduleIds);
    }

    @Transactional
    public ModuleDTO findById(Long id) {
        Optional<Module> moduleOpt = moduleRepository.findById(id);
        return moduleOpt.map(this::convertToDto).orElse(null);
    }

    @Transactional
    public ModuleDTO add(ModuleDTO moduleDto) {
        Module module = convertToEntity(moduleDto);
        Module savedModule = moduleRepository.save(module);
        Topic topic = topicRepository.findByTopicId(moduleDto.getTopicId());
        topic.addModule(savedModule);
        topicRepository.save(topic);
        return convertToDto(savedModule);
    }

    @Transactional
    public ModuleDTO update(ModuleDTO moduleDto) {
        Module oldModule = moduleRepository.findModuleById(moduleDto.getId());
        if (oldModule != null) {
            System.out.println("OldModule: " + oldModule);
            oldModule.setId(moduleDto.getId());
            oldModule.setTitle(moduleDto.getTitle());
            oldModule.setContent(moduleDto.getContent());
            oldModule.setTopic(topicRepository.findByTopicId(moduleDto.getTopicId()));
            Module updatedModule = moduleRepository.save(oldModule);
            return convertToDto(updatedModule);
        } else {
            throw new ResourceNotFoundException("Module not found with id " + moduleDto.getId());
        }
    }

    @Transactional
    public void delete(Long id) {
        moduleRepository.deleteById(id);
    }

    @Transactional
    public List<ModuleDTO> findAllByTopicId(Long topicId) {
        List<Module> modules = moduleRepository.findAllByTopicTopicId(topicId);
        return modules.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ModuleDTO convertToDto(Module module) {
        List<QuizDTO> quizDTOs = module.getQuizzes().stream()
                .map(this::convertQuizToDto)
                .collect(Collectors.toList());

        return ModuleDTO.builder()
                .id(module.getId())
                .title(module.getTitle())
                .content(module.getContent())
                .topicId(module.getTopic().getTopicId())
                .quizzes(quizDTOs)
                .build();
    }

    private QuizDTO convertQuizToDto(Quiz quiz) {
        return QuizDTO.builder()
                .id(quiz.getId())
                .question(quiz.getQuestion())
                .correctAnswer(quiz.getCorrectAnswer())
                // Add other fields as necessary
                .build();
    }


    private Module convertToEntity(ModuleDTO moduleDto) {
        Module module = Module.builder()
                .id(moduleDto.getId())
                .title(moduleDto.getTitle())
                .content(moduleDto.getContent())
                .topic(topicRepository.findByTopicId(moduleDto.getTopicId()))
                .build();
        
        if (moduleDto.getQuizzes() != null) {
            moduleDto.getQuizzes().stream()
                    .map(this::convertQuizToEntity)
                    .forEach(module::addQuiz);
        }
        return module;
    }

    private Quiz convertQuizToEntity(QuizDTO quizDto) {
        return Quiz.builder()
                .id(quizDto.getId())
                .question(quizDto.getQuestion())
                .correctAnswer(quizDto.getCorrectAnswer())
                .options(quizDto.getOptions())
                .questionType(quizDto.getQuestionType())
                .matchingOptions(quizDto.getMatchingOptions())
                .build();
    }

    public ModuleDTO findByTitleAndTopicTopicId(String moduleTitle, Long topicId) {
        return moduleRepository.findByTitleAndTopicTopicId(moduleTitle, topicId);
    }

    public void deleteByTopicId(Long id) {
        moduleRepository.deleteByTopicTopicId(id);
    }
}
