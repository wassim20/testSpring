package com.elokencia.springApp.repository;

import com.elokencia.springApp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> { }