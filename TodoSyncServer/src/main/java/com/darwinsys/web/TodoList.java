package com.darwinsys.web;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.darwinsys.todo.model.Task;

@Named("todoList")
public class TodoList {
	@PersistenceContext(unitName="todo")
	EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public List<Task> getResultList() {
		System.out.println("TodoList.getResultList()");
		@SuppressWarnings("unchecked")
		final List<Task> resultList = entityManager.createQuery("from Task t").getResultList();
		System.out.println("List size = " + resultList.size());
		return resultList;
	}
}
