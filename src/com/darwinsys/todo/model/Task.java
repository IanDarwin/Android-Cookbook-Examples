package com.example.myaccount.model;

import java.util.Date;

/** One ToDo item or "task".
 * See http://todotxt.com/ and https://github.com/ginatrapani/todo.txt-cli/wiki/The-Todo.txt-Format.
 * @author Ian Darwin
 */
public class Task {
	
	long id;
	String name;	// what to do
	Date creationDate; // when you decided you had to do it
	String project;		// what this task is part of
	String context;	// where to do it
	Date dueDate;	// when to do it by
	Date completedDate; // when you actually did it

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
}
