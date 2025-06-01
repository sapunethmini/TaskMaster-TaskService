package org.example.service.Impl;
import org.example.dto.Priority;
import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.TaskEntity;
import org.example.model.NotificationEvent;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    private TaskEntity convertToEntity(TaskResponseDto dto) {
        TaskEntity task = new TaskEntity();
        task.setTeam(dto.getTeam());
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setCategory(dto.getCategory());
        task.setPriority(dto.getPriority());
        task.setUserId(dto.getUserId());
        task.setCreatedAt(dto.getCreatedAt());
        task.setUpdatedAt(dto.getUpdatedAt());
        task.setDueDate(dto.getDueDate());
        task.setDuration(dto.getDuration());
        return task;
    }

    private TaskEntity convertToEntity(TaskRequestDto dto) {
        TaskEntity task = new TaskEntity();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setCategory(dto.getCategory());
        task.setPriority(dto.getPriority());
        task.setUserId(dto.getUserId());
        task.setTeam(dto.getTeam());
        task.setDuration(dto.getDuration());
        task.setDueDate(dto.getDueDate());
        return task;
    }

    private TaskResponseDto convertToDto(TaskEntity task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCategory(task.getCategory());
        dto.setTeam(task.getTeam());
        dto.setDuration(task.getDuration());
        dto.setDueDate(task.getDueDate());
        dto.setUserId(task.getUserId());
        try {
            dto.setPriority(task.getPriority());
        } catch (Exception e) {
            dto.setPriority(Priority.LOW);
        }
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }

    // Update entity from DTO
    private void updateEntityFromDto(TaskRequestDto dto, TaskEntity task) {
        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        if (dto.getCategory() != null) task.setCategory(dto.getCategory());
        if (dto.getPriority() != null) task.setPriority(dto.getPriority());
        if (dto.getUserId() != null) task.setUserId(dto.getUserId());
        if (dto.getTeam() != null) task.setTeam(dto.getTeam());
        if (dto.getDuration() != null) task.setDuration(dto.getDuration());
        if (dto.getDueDate() != null) task.setDueDate(dto.getDueDate());
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskDto) {
        TaskEntity task = convertToEntity(taskDto);
        TaskEntity savedTask = taskRepository.save(task);
        sendTaskNotification("TASK_CREATED", savedTask);
        return convertToDto(savedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return convertToDto(task);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskDto) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        updateEntityFromDto(taskDto, task);
        TaskEntity updatedTask = taskRepository.save(task);
        sendTaskNotification("TASK_UPDATED", updatedTask);

        return convertToDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        sendTaskNotification("TASK_DELETED", task);
        taskRepository.delete(task);
    }

    @Override
    public List<TaskResponseDto> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByCategory(String category) {
        if (category.startsWith("{") && category.endsWith("}")) {
            category = category.substring(1, category.length() - 1);
        }

        List<TaskEntity> tasks = taskRepository.findByCategory(category);
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByUserIdAndCategory(Long userId, String category) {
        return taskRepository.findByUserIdAndCategory(userId, category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByTeamAndStatus(String team, String status) {
        System.out.println("Service called with - Team: '" + team + "', Status: '" + status + "'");

        // First, let's see all tasks to debug
        List<TaskEntity> allTasks = taskRepository.findAll();
        System.out.println("Total tasks in database: " + allTasks.size());

        // Print some sample data for debugging
        allTasks.stream().limit(3).forEach(task ->
                System.out.println("Sample task - Team: '" + task.getTeam() + "', Status: '" + task.getStatus() + "'")
        );

        // Try the original query
        List<TaskEntity> filteredTasks = taskRepository.findByTeamAndStatus(team, status);
        System.out.println("Filtered tasks found: " + filteredTasks.size());

        return filteredTasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getAllTasksSortedByCreatedDateDesc() {
        return taskRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getAllTasksSortedByCreatedDateAsc() {
        return taskRepository.findAllByOrderByCreatedAtAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getAllTasksSortedByPriorityDesc() {
        return taskRepository.findAllByOrderByPriorityDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getAllTasksSortedByPriorityAsc() {
        return taskRepository.findAllByOrderByPriorityAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getAllTasksSortedByTitleAsc() {
        return taskRepository.findAllByOrderByTitleAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getAllTasksSortedByStatusAsc() {
        return taskRepository.findAllByOrderByStatusAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByUserIdSortedByPriority(Long userId) {
        return taskRepository.findByUserIdOrderByPriorityDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByUserIdSortedByCreatedDate(Long userId) {
        return taskRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByCategorySortedByPriority(String category) {
        return taskRepository.findByCategoryOrderByPriorityDesc(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByStatusSortedByCreatedDate(String status) {
        return taskRepository.findByStatusOrderByCreatedAtAsc(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getAllTasksSorted(String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return taskRepository.findAll(sort).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long getTaskCountByStatus(String status) {
        return taskRepository.countByStatus(status);
    }


    @Override
    public Long getTaskCountByTeam(String team) {

        return taskRepository.countByTeam(team);
    }



    @Override
    public Long getTotalTaskCount() {
        return taskRepository.count();
    }


    @Override
    public Long getTaskCountByTeamAndStatus(String team, String status) {
        return taskRepository.countByTeamAndStatus(team, status);
    }


    @Override
    public Long getTaskCountByTeamAndPriority(String team, Priority priority) {
        return taskRepository.countByTeamAndPriority(team, priority);
    }

    @Override
    public List<TaskResponseDto> getTasksByTeamAndPriority(String team, Priority priority) {
        return taskRepository.findByTeamAndPriority(team, priority).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private void sendTaskNotification(String eventType, TaskEntity task) {
        try {
            NotificationEvent event = new NotificationEvent(
                eventType,
                task.getUserId(),
                getUserEmail(task.getUserId()),
                getNotificationTitle(eventType),
                getNotificationMessage(eventType, task)
            );
            
            event.setTaskId(task.getId());
            event.setTaskTitle(task.getTitle());
            
            kafkaTemplate.send("task-events", event);
        } catch (Exception e) {
            System.err.println("Failed to send task notification: " + e.getMessage());
        }
    }
    
    private String getUserEmail(Long userId) {
        // TODO: Implement proper user email retrieval
        // For now, return a placeholder email
        return "user" + userId + "@example.com";
    }
    
    private String getNotificationTitle(String eventType) {
        switch (eventType) {
            case "TASK_CREATED": return "New Task Created";
            case "TASK_UPDATED": return "Task Updated";
            case "TASK_DELETED": return "Task Deleted";
            default: return "Task Notification";
        }
    }
    
    private String getNotificationMessage(String eventType, TaskEntity task) {
        switch (eventType) {
            case "TASK_CREATED": 
                return "A new task '" + task.getTitle() + "' has been created.";
            case "TASK_UPDATED": 
                return "Task '" + task.getTitle() + "' has been updated.";
            case "TASK_DELETED": 
                return "Task '" + task.getTitle() + "' has been deleted.";
            default: 
                return "Task '" + task.getTitle() + "' has been modified.";
        }
    }
}