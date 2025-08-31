package com.elokencia.springApp.controller;

import com.elokencia.springApp.dto.ProjectDTO;
import com.elokencia.springApp.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // GET /projects - List all projects with tasks
    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    // GET /projects/{id} - Get project by ID with nested tasks
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /projects - Create new project
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        try {
            ProjectDTO saved = projectService.createProject(projectDTO);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /projects/{id} - Update project
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        if (!id.equals(projectDTO.id())) {
            throw new RuntimeException("Project not found with ID: " + projectDTO.id());
        }
        try {
            ProjectDTO updated = projectService.updateProject(projectDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            throw new RuntimeException("Can't Update ID: " + projectDTO.id());

        }
    }

    // DELETE /projects/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}