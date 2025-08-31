package com.elokencia.springApp.services;

import com.elokencia.springApp.dto.TaskDTO;
import com.elokencia.springApp.model.Task;
import com.elokencia.springApp.model.User;
import com.elokencia.springApp.model.Project;
import com.elokencia.springApp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserService userService;
    private final ProjectService projectService;

    public TaskService(TaskRepository taskRepo, UserService userService, ProjectService projectService) {
        this.taskRepo = taskRepo;
        this.userService = userService;
        this.projectService = projectService;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TaskDTO> getTaskById(Long id) {
        return taskRepo.findById(id)
                .map(this::mapToDTO);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        User assignee = userService.getUserEntityById(taskDTO.assigneeId());
        Project project = getProjectEntityById(taskDTO.projectId());

        Task task = new Task();
        task.setTitle(taskDTO.title());
        task.setStatus(taskDTO.status());
        task.setAssignee(assignee);
        task.setProject(project);

        Task saved = taskRepo.save(task);
        return mapToDTO(saved);
    }

    public TaskDTO updateTask(TaskDTO taskDTO) {
        Task existing = taskRepo.findById(taskDTO.id())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User assignee = userService.getUserEntityById(taskDTO.assigneeId());
        Project project = getProjectEntityById(taskDTO.projectId());

        existing.setTitle(taskDTO.title());
        existing.setStatus(taskDTO.status());
        existing.setAssignee(assignee);
        existing.setProject(project);

        Task updated = taskRepo.save(existing);
        return mapToDTO(updated);
    }

    public void deleteTask(Long id) {
        if (taskRepo.existsById(id)) {
            taskRepo.deleteById(id);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    // Map Task entity → TaskDTO
    private TaskDTO mapToDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getProject().getId(),
                task.getAssignee().getId()
        );
    }

    private Project getProjectEntityById(Long projectId) {
        return projectService.getProjectEntityById(projectId);
    }
}