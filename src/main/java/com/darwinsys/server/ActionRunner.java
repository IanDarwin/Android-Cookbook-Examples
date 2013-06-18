package com.darwinsys.server;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/** Quartz-scheduled bean to run Conformance Checking and other periodic stuff. */
//@MessageDriven(activationConfig = {
//	// The cronTrigger expression attempts to run once/day at 1030
//	@ActivationConfigProperty(
//		propertyName="cronTrigger",
//		propertyValue="0 30 10 * * ?")
//})
public class ActionRunner implements Job {
	
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		System.out.println("ActionRunner.execute()");
	}
}
