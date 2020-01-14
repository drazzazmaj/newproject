package com.drazza.ppmtool.exceptions;

public class ProjectIdExceptionResponse {
  private String projectIdentifier;
  public ProjectIdExceptionResponse(String projectIdentifier) {
	  System.out.println("poziv ProjectIdExceptionResponse"+ projectIdentifier);
	  this.projectIdentifier = projectIdentifier;
  }
public String getProjectIdentifier() {
	return projectIdentifier;
}
public void setProjectIdentifier(String projectIdentifier) {
	this.projectIdentifier = projectIdentifier;
}
  
}
