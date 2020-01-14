package com.drazza.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drazza.ppmtool.domain.Backlog;
import com.drazza.ppmtool.domain.Project;
import com.drazza.ppmtool.domain.User;
import com.drazza.ppmtool.exceptions.ProjectIdException;
import com.drazza.ppmtool.exceptions.ProjectNotFoundException;
import com.drazza.ppmtool.repository.BacklogRepository;
import com.drazza.ppmtool.repository.ProjectRepository;
import com.drazza.ppmtool.repository.UserRepository;

@Service
public class ProjectService {
  @Autowired
  private ProjectRepository projectRepository;
  
  @Autowired
  private BacklogRepository backlogRepository;
  
  @Autowired
  private UserRepository userRepository;
  
  public Project saveOrUpdateProject(Project project, String username) {
	
	  //project.getId != null
	  //find by db if -> null
	  if (project.getId() != null) {
		 Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier()); 
	  
		  if(existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
			  throw new ProjectNotFoundException("Project not found in your account");
		  } else if(existingProject==null) {
		  throw new ProjectNotFoundException("Project with ID: '"+ project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
		  }
	  }
	  
	  try {
		    
		  User user = userRepository.findByUsername(username);
		  
		  project.setUser(user);
		  project.setProjectLeader(user.getUsername());
		  project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
		  if(project.getId() == null) {
			  Backlog backlog = new Backlog();
			  project.setBacklog(backlog);
			  backlog.setProject(project);
			  backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
		  }
		  if (project.getId() != null) {
			 project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
		  }
	      return projectRepository.save(project);
	  } catch (Exception e) {
		   System.out.println("Exception");
		  throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+" ' already exist");
	  }
  }
  
  public Project findProjectByIdentifier(String projectId, String username) {
	  Project project =  projectRepository.findByProjectIdentifier(projectId);
	  if (project == null) {
		  throw new ProjectIdException("Project ID '"+ projectId.toUpperCase()+" ' doesn't exist");
	  }
	  if (!project.getProjectLeader().equals(username)) {
		  throw new ProjectNotFoundException("Project not found in your account");
	  }
	  return project;
  }
  
  public Iterable<Project> findAllProjects(String username){
	  return projectRepository.findAllByProjectLeader(username); //findAll();
  }
  
  public void deleteProjectByIdentifier(String projectId, String username) {
	  Project project = projectRepository.findByProjectIdentifier(projectId);
	  /*if (project == null) {
		  throw new ProjectIdException("Cannot find project '"+ projectId + "'.This project does not exist.");
	  }*/
	  projectRepository.delete(findProjectByIdentifier(projectId, username));
  }
  
}
