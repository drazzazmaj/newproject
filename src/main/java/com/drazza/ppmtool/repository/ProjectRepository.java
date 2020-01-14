package com.drazza.ppmtool.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drazza.ppmtool.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository <Project, Long> {
   
	Project findByProjectIdentifier(String projectId);
    
	@Override
    Iterable<Project> findAll();
	
	Iterable<Project> findAllByProjectLeader(String username);
}