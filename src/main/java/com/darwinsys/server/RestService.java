package com.darwinsys.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.example.myaccount.model.Task;



/**
 * This implements only the web-service part of the ToDo server.
 * It uses a JPA model to access the data.
 * @author Ian Darwin
 */
@Path("/")
@ApplicationScoped
public class RestService {
	
	private static final String TEST_MESSAGE_STRING = "This is a test message for ";

	private static final String TEST_ALERT_STRING = "This is a test alert! Do not act upon it.";

	@Inject
	EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	boolean
		/** True if we want to print/log call activity */
		trace = true,
		/** True if we want to send Alert text to test the Client's alerting code. */
		alert = false;

	private boolean debug;

	public static final String WEB_SERVICE_VERSION = "2.0";
	
	/** SimpleDateFormat used for parsing dates; note that it is NOT THREAD SAFE, so
	 * usage must be synchronized!
	 */
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");
	
	static {
		System.out.println("RazorbackServer.RestService class loaded.");
	}
	public RestService() {
		System.out.println("RazorbackServer.RestService instantiated.");
	}
	
	/** Diagnostic printing */
	void trace(String mesg) {
		if (trace) {
			System.out.println(mesg);
		}
	}
	
	@GET @Path("")
	@Produces(MediaType.TEXT_HTML)
	public String getDefaultPath() {
		trace("GET RestService.getDefaultPath()");
		return getIndex();
	}

	/** Since we map "/" to this component, we have to handle trivia like /index.html here... */
	@GET @Path("/index.html")
	@Produces(MediaType.TEXT_HTML)
	public String getIndex() {
		trace("GET RestService.getIndex()");
		return "<html><head><title>Razorback Server</title></head>" +
			"<body><h1>Yeah.</h1><p>The server is running. That's all I can tell you.</p></body></html>";
	}

	/** This is a system-status "ping" type service */
	@GET @Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStatus() {
		trace("GET RestService.getStatus()");
		// May also wish to add:
		// "unreported_exceptions": ", [ list of exceptions ] // SHOULD TIMESTAMP EACH ONE!
		// "uptime": "(19 weeks, 5 days, 22:29:29)" 
		return String.format(
				"{" +
					"\"database_status\": \"working\", " +
					"\"web_service_version: \"%s\", " +
					"\"time_on_server\": \"%s\", " +
					"\"options/trace\": \"%s\", " +
					"\"status\": \"W00table\", " +
					"\"options/alert\": \"%s\", " +
				"}",
				WEB_SERVICE_VERSION,
				new Date(),
				trace,
				alert 
		);
	}
	
	/** Options settings - simple for now */
	@GET @Path("/options/{option}/{value}")
	@Produces("text/plain")
	public String setOption(@PathParam("option")String option, @PathParam("value")boolean value) {
		// Do NOT use trace() here!
		if ("trace".equals(option)) {
			System.out.println("RestService.set Trace = " + value + ")");
			this.trace = value;
			return "ok; set to " + this.trace;
		} else if ("debug".equals(option)) {
			System.out.println("RestService.set debug = " + value + ")");
			this.debug = value;
			return "ok; set to " + this.debug;
		} else if ("alerts".equals(option)) {
			alert = value;
			return "ok; all items will now return: " + TEST_ALERT_STRING;
		}
		return "ok; WARNING: unknown option: " + option;
	}
	
	/** Used to upload a todo item
	 * @throws ParseException on certain invalid inputs
	 *  */
	@POST @Path("/todo/{userName}/item")
	@Produces("text/plain")
	@Consumes({"application/x-www-form-urlencoded", "multipart/form-data"})
	public Response SaveTask(
		@PathParam("userName")String userName, // aka patient id!
		MultivaluedMap<String, String> params) throws ParseException {
		
		final String TAG = "RestService.postNormalitem(): ";
		
		trace("POST /todo/" + userName + "/items");
		
		Task item = new Task();
		
		try {
			entityManager.persist(item);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.notModified("FAIL-persistence: " + e).build();
		}
		
		try {
			return Response.created(new URI(String.format("/todo/%s/items/%d", userName, item.getId()))).build();
		} catch (URISyntaxException e) {
			// CANT HAPPEN
			System.err.println("IMPOSSIBLE ERROR: " + e);
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	/** Used to download a item BY item ID */
	@GET @Path("/todo/items/{itemId}")
	public String getOneItem( @PathParam("itemId")long id) {
		trace(String.format("GET /todo/%s/item %d", id));

		Task r = entityManager.find(Task.class, id);
		return r.toString();
	}
}
