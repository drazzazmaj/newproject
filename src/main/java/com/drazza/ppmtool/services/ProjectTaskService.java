package com.drazza.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drazza.ppmtool.domain.Backlog;
import com.drazza.ppmtool.domain.Project;
import com.drazza.ppmtool.domain.ProjectTask;
import com.drazza.ppmtool.exceptions.ProjectNotFoundException;
import com.drazza.ppmtool.repository.BacklogRepository;
import com.drazza.ppmtool.repository.ProjectRepository;
import com.drazza.ppmtool.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {
  @Autowired
  private ProjectTaskRepository projectTaskRepository;
  @Autowired
  private BacklogRepository backlogRepository;
  @Autowired
  private ProjectRepository projectRepository;
  
  @Autowired
  private ProjectService projectService;
  
  public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
	  //try {
		  //Project Tasks to be added to a specific project, project != null, bl exists
		  Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier,username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);
		  //set bl to pt
		  projectTask.setBacklog(backlog);
		  //we want our project sequence to be like this IDPRO-1, IDPRO-2....100 101
		  Integer BacklogSequence = backlog.getPTSequence();
		  //update the BL sequence
		  BacklogSequence++;
		  backlog.setPTSequence(BacklogSequence);
		  //backlogRepository.save(backlog);//ne treba, ovo ce da odradi projectTaskRepository.save(projectTask);
		  //Add Sequence to project task
		  projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
		  projectTask.setProjectIdentifier(projectIdentifier);
		  //initial priority when priority null
		  if (projectTask.getPriority() == null || projectTask.getPriority()  == 0 ) {// in the future we need projectTask.getPriority()= 0 to handle the form
			projectTask.setPriority(3);
	      }
		  //initial status when status is null
		  if (projectTask.getStatus()==""|| projectTask.getStatus()==null) {
			  projectTask.setStatus("TO_DO");
		  }
		  return projectTaskRepository.save(projectTask);
	  /*} catch(Exception ex) {
		  throw new ProjectNotFoundException("Project not found");*/
		//Exceptions: Project not found
		  /*{
			  "ProjectNotFound": "Project not found"
		  }*/
		  
	  //}
  }
  
  public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {
	  
	  projectService.findProjectByIdentifier(backlog_id, username);
	  /*Project project = projectRepository.findByProjectIdentifier(backlog_id);
	  if (project == null) {
		  throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
	  }*/
	  return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
  }
  
  public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
	  //make sure we are searching on an existing backlog
	  projectService.findProjectByIdentifier(backlog_id, username);
	  /*Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
	  if (backlog == null) {
		  throw new ProjectNotFoundException("Project with ID: '"+ backlog_id +"' does not exist");
	  }*/
	  //make sure that out task exists 
	  ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
	  if (projectTask == null) {
		  throw new ProjectNotFoundException("Project task with ID: '"+ pt_id+"' not found");
	  }
	  //make sure that backlog/project id in the path corresponds to the right projects (task belongs to the project)
	  if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
		  throw new ProjectNotFoundException("Project task '"+pt_id+"'does not exists in project:"+backlog_id);
	  }
	  return projectTaskRepository.findByProjectSequence(pt_id);
  }
  //update project task
  public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
	  //find existing project task
	  //ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
	  ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
	  //replace it with updated task
	  projectTask = updatedTask;
	  //save update
	  projectTaskRepository.save(projectTask);
	  return projectTask;
  }
  
  public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
	  ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
	  
/*	 Backlog backlog = projectTask.getBacklog();
	 List<ProjectTask> pts = backlog.getProjectTasks();
	 pts.remove(projectTask);
	 backlogRepository.save(backlog);//da bi obrisao project task mora najpre da se azurira master backlog
*/	 projectTaskRepository.delete(projectTask);
  }

  

  
 
}
