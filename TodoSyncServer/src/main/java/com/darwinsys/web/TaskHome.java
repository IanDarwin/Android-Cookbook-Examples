package com.darwinsys.web;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.darwinsys.todo.model.Task;

@Stateful @Named("taskHome") @RequestScoped
public class TaskHome {
	@PersistenceContext(unitName="todo")
	EntityManager entityManager;

	Task task = new Task();

	public TaskHome() {
		System.out.println("TaskHome.TaskHome()");
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String addTask() {
		System.out.println("TaskHome.addTask()");
		entityManager.persist(task);
		System.out.println("Saved task " + task);
		return "todoList.xhtml";
	}
	
	public String[] getPriorities() {
		System.out.println("TaskHome.getPriorities()");
		return new String[]{"h","m","l"};
	}

	public Task getTask() {
		System.out.println("TaskHome.getTask()");
		return task;
	}

	public void setTask(Task task) {
		System.out.println("TaskHome.setTask()");
		this.task = task;
	}
}
