package com.elokencia.springApp.services;

import com.elokencia.springApp.dto.TaskDTO;
import com.elokencia.springApp.dto.UserDTO;
import com.elokencia.springApp.model.Task;
import com.elokencia.springApp.model.User;
import com.elokencia.springApp.repository.TaskRepository;
import com.elokencia.springApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private  UserRepository userRepo;
    private TaskRepository taskRepository;



    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepo.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()));
    }
    public Optional<Map<String, Object>> getUserByIdWithTasks(Long id) {
        return userRepo.findById(id)
                .map(user -> {
                    List<TaskDTO> tasks = user.getTasks().stream()
                            .map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getStatus(),task.getProject().getId(),task.getAssignee().getId()))
                            .collect(Collectors.toList());

                    return Map.of(
                            "user", new UserDTO(user.getId(), user.getName(), user.getEmail()),
                            "tasks", tasks
                    );
                });
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        User saved = userRepo.save(user);
        return new UserDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    public UserDTO updateUser(UserDTO userDTO) {
        if (!userRepo.existsById(userDTO.id())) {
            throw new RuntimeException("User not found");
        }
        User user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        User updated = userRepo.save(user);
        return new UserDTO(updated.getId(), updated.getName(), updated.getEmail());
    }

    public void deleteUser(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Helper: Find User entity by ID (for use in other services)
    public User getUserEntityById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }


}