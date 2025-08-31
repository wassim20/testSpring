package com.elokencia.springApp.dto;

import java.util.List;

public record ProjectDTO(Long id, String name, String description, Long ownerId, List<TaskDTO> tasks) { }