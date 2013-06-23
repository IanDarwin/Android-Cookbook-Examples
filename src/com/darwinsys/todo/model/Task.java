package com.example.myaccount.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** One ToDo item or "task".
 * See http://todotxt.com/ and https://github.com/ginatrapani/todo.txt-cli/wiki/The-Todo.txt-Format.
 * @author Ian Darwin
 */
@Entity
public class Task {
	
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final char SPACE = ' ';

	private static final char PROJECT = '+', CONTEXT = '@';
	long id;
	String name;	// what to do
	Date creationDate; // when you decided you had to do it
	String project;		// what this task is part of
	String context;	// where to do it
	Date dueDate;	// when to do it by
	boolean complete;
	Date completedDate; // when you actually did it
	
	public Task() {
		creationDate = new Date();
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
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
	@Temporal(TemporalType.DATE)
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
	
	@Temporal(TemporalType.DATE)
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public boolean isComplete() {
		return complete;
	}

	/**
	 * set completion to true or false, and side-effect set completion date
	 * to today or to null, but if setting to true and client already set
	 * completion data, leave well enough alone.
	 * @param complete
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
		if (complete) {
			if (getCompletedDate() == null) {
				completedDate = new Date();
			}
		} else {
			setCompletedDate(null);
		}
	}
	
	@Temporal(TemporalType.DATE)
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
		complete = completedDate != null;
	}
	
	/** ToString converts to String but in todo.txt format! 
	 * A fully-fleshed-out example from todotxt.com:
	 * x 2011-03-02 2011-03-01 Review Tim's pull request +TodoTxtTouch @github 
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (isComplete()) {
			sb.append('x').append(SPACE).append(dateFormat.format(getCompletedDate())).append(SPACE);
		}
		sb.append(dateFormat.format(getCreationDate()));
		sb.append(SPACE);
		sb.append(name);
		if (getProject() != null) {
			sb.append(SPACE).append(PROJECT).append(project);
		}
		if (getContext() != null) {
			sb.append(SPACE).append(CONTEXT).append(context);
		}
		return sb.toString();
	}
}
