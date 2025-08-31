package com.elokencia.springApp.dto;

public record TaskDTO(Long id, String title, String status, Long projectId, Long assigneeId) { }
