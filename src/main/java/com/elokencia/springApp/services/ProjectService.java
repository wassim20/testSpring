package com.elokencia.springApp.services;

import com.elokencia.springApp.dto.ProjectDTO;
import com.elokencia.springApp.dto.TaskDTO;
import com.elokencia.springApp.model.Project;
import com.elokencia.springApp.model.User;
import com.elokencia.springApp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepo, UserService userService) {
        this.projectRepo = projectRepo;
        this.userService = userService;
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        return projectRepo.findById(id)
                .map(this::mapToDTO);
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        User owner = userService.getUserEntityById(projectDTO.ownerId());

        Project project = new Project();
        project.setName(projectDTO.name());
        project.setDescription(projectDTO.description());
        project.setOwner(owner);

        Project saved = projectRepo.save(project);
        return mapToDTO(saved);
    }

    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        Project existing = projectRepo.findById(projectDTO.id())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User owner = userService.getUserEntityById(projectDTO.ownerId());

        existing.setName(projectDTO.name());
        existing.setDescription(projectDTO.description());
        existing.setOwner(owner);

        Project updated = projectRepo.save(existing);
        return mapToDTO(updated);
    }

    public void deleteProject(Long id) {
        if (projectRepo.existsById(id)) {
            projectRepo.deleteById(id);
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    // Map Project entity → ProjectDTO
    private ProjectDTO mapToDTO(Project project) {
        List<TaskDTO> taskDTOs = project.getTasks().stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getProject().getId(),
                        task.getAssignee().getId()
                ))
                .collect(Collectors.toList());

        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner().getId(),
                taskDTOs
        );
    }
    public Project getProjectEntityById(Long id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}