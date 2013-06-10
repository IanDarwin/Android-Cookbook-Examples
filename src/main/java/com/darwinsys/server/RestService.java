package org.ehealthinnovation.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import model.Patient;
import model.reading.BloodGlucoseReading;
import model.reading.BloodPressureReading;
import model.reading.EcgReading;
import model.reading.PulseReading;
import model.reading.Reading;
import model.reading.ReadingType;
import model.reading.SymptomReading;
import model.reading.WeightReading;

import org.ehealthinnovation.entityframework.EntityHome;

/**
 * This implements only the web-service part of the CHF server.
 * It uses a JPA model to access the data.
 * @author Ian Darwin
 */
@Path("/")
@ApplicationScoped
public class RestService {
	
	private static final String TEST_MESSAGE_STRING = "This is a test message for ";

	private static final String TEST_ALERT_STRING = "This is a test alert! Do not act upon it.";

	@Inject
	ReadingHome readingBean; 						// Believed to be thread-safe.
	
	boolean
		/** True if we want to print/log call activity */
		trace = true,
		/** True if we want to send Alert text to test the Client's alerting code. */
		alert = false;

	private boolean debug;
	
	private Date serviceStarted = new Date();

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
			return "ok; all readings will now return: " + TEST_ALERT_STRING;
		}
		return "ok; WARNING: unknown option: " + option;
	}
	
	/** Used to upload a reading 
	 * @throws ParseException on certain invalid inputs
	 *  */
	@POST @Path("/patients/{userName}/readings")
	@Produces("text/plain")
	@Consumes({"application/x-www-form-urlencoded", "multipart/form-data"})
	public Response postNormalReading(
		@PathParam("userName")String userName, // aka patient id!
		MultivaluedMap<String, String> params) throws ParseException {
		
		final String TAG = "RazorbackServer.postNormalReading(): ";
		
		trace("POST /patients/" + userName + "/readings");
		String type = params.getFirst("type");
		if (type == null) {
			final String message = "no type field";
			System.out.println(TAG + message);
			return Response.notModified(message).build();
		}
		ReadingType readingType;
		try {
			readingType = ReadingType.valueFromString(type);
		} catch (RuntimeException e1) {
			final String message = "Invalid type field " + type;
			System.err.println(TAG + message);
			return Response.notModified(message).build();
		}
		System.out.printf("RestService.postNormalReading(): Update %s reading for %s%n", readingType, userName);

		// Dump map for debugging; remove when done!
		if (debug) {
			final Set<String> keys = params.keySet();
			for (String k : keys) {
				String v = params.getFirst(k);
				System.out.printf("%s -> %s%n", k, v);
			}
		}
		
		Reading reading = null;
		switch(readingType) {
		case BloodGlucose:
			BloodGlucoseReading bg = new BloodGlucoseReading();
			reading = bg;
			bg.setMeasurement(Double.valueOf(params.get("reading[measurement]").get(0)));
			bg.setUnits(params.get("reading[units]").get(0));
			break;
		case BloodPressure:
			BloodPressureReading bp = new BloodPressureReading();
			reading = bp;
			bp.setDbp(Integer.valueOf(params.get("reading[dbp]").get(0).trim())); // diastolic
			bp.setSbp(Integer.valueOf(params.get("reading[sbp]").get(0).trim())); // systolic
			bp.setMap(Integer.valueOf(params.get("reading[map]").get(0).trim()));
			break;
		case ECG:
			EcgReading ecg = new EcgReading();
			reading = ecg;
			// XXX FINISH ME - get details
			break;
		case Pulse:
			PulseReading pr = new PulseReading();
			reading = pr;
			pr.setMeasurement(Integer.valueOf(params.get("reading[measurement]").get(0).trim()));
			pr.setUnits(params.get("reading[units]").get(0));
			break;
		case Symptom:
			SymptomReading sr = new SymptomReading();
			reading = sr;
			int answers[] = new int[20];
			for (int i = 0; i < answers.length; i++) {
				final List<String> list = params.get("response" + i);
				if (list == null) {
					break;
				}
				String raw = list.get(0);
				if (raw == null) {
					break;
				}
				System.out.println("ANSWER " + i + " " + raw);
				answers[i] = Integer.valueOf(raw.trim());
			}
			break;
		case Weight:
			WeightReading w = new WeightReading();
			reading = w;
			w.setMeasurement(Double.valueOf(params.get("reading[measurement]").get(0).trim()));
			w.setUnits(params.get("reading[units]").get(0));
			break;
		default:
			System.err.println("Unhandled case - reading type from client: " + readingType);
			return Response.serverError().build();
		}
		
		// Common fields
		Patient pat = new Patient();
		pat.setId(100);						// XXX HUGE HACK
		pat.setLogin(userName);
		reading.setPatient(pat);
		reading.setRid(Long.valueOf(params.get("reading[rid]").get(0)));
		reading.setMeasurementContext(params.get("reading[measurement_context]").get(0));
		reading.setRelationship(params.get("reading[relationship]").get(0));
		// Use a list here because the created_at can be null, and otherwise we get an NPE
		// There should be only one element.
		final List<String> dateCreatedParams = params.get("reading[created_at]");	
		if (dateCreatedParams != null) {
			final String dateEntered = dateCreatedParams.get(0).trim();
			synchronized(df) {	// see comments on field.
				reading.setCreatedAt(df.parse(dateEntered));
			}
		}
		final String dateMeasured = params.get("reading[measured_at]").get(0);
		synchronized(df) {
			reading.setMeasuredAt(df.parse(dateMeasured));
		}
		reading.setSerialNumber(params.get("reading[serial_number]").get(0));
		reading.setTimeSource(Integer.valueOf(params.get("reading[time_source]").get(0)));
		
		try {
			readingBean.persist(reading);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.notModified("FAIL-persistence: " + e).build();
		}
		
		try {
			return Response.created(new URI(String.format("/patients/%s/readings/%d", userName, reading.getRid()))).build();
		} catch (URISyntaxException e) {
			// CANT HAPPEN
			System.err.println("IMPOSSIBLE ERROR: " + e);
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	/** Used to download a reading BY READING ID */
	@GET @Path("/patients/{userName}/readings/{readingId}")
	public String getOneReading(
			@PathParam("userName")String userName,
			@PathParam("readingId")long rId) {
		trace(String.format("GET /patients/%s/reading %d", userName, rId));

		Reading r = readingBean.findReading(userName, rId);
		return r.toString();
	}
	
	/** Called occasionally to get the patient's report (containing Goals, Status, an Alert if any), ... */
	@GET @Path("/patients/{userName}/report")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPatientReport(@PathParam("userName")String userName) {
		trace("GET PatientReport " + userName);
		// XXX ALL VALUES ARE FAKE AT PRESENT
		return String.format(
				"{\n" +
					"\"weight_goal_min\":    150.0,\n" +
					"\"weight_goal_max\":    160.0,\n" +
					"\"weight_goal_delta\":  2.0,\n" +
					"\"bp_goal_min_sys\":    80,\n" +
					"\"bp_goal_min_dia\":    40,\n" +
					"\"bp_goal_max_sys\":    180,\n" +
					"\"bp_goal_max_dia\":    100,\n" +
					"\"pulse_goal_min\":     40.0,\n" +
					"\"pulse_goal_max\":     150.0,\n" +
					"\"rid\":                %d,\n" +
					"\"summary\":           \"%s\",\n" +
					"\"alert\":             \"%s\"\n" +
				"}\n",
				System.currentTimeMillis(),
				TEST_MESSAGE_STRING + userName + ".",
				alert ? TEST_ALERT_STRING : ""
		);
	}
	
	/** Called from the client's HttpLogger */
	@POST
	@Path("/client_log_messages")
	@Consumes("application/x-www-form-urlencoded")
	public Response postLogMessage(MultivaluedMap<String, String> params) throws ParseException {
		// You may think that these key names take the notion of "self-
		// identifying data" too far. Or not. Try XML :-) 
		String message = params.getFirst("client_log_message[message]");
		String level = params.getFirst("client_log_message[log_level]");
		String patientId = params.getFirst("client_log_message[patient_id]");
		String clientOs = params.getFirst("client_log_message[platform_revision]");
		String deviceId = params.getFirst("client_log_message[device_id]");
		System.out.println("ClientLog: " + level + ": " + message);
		System.out.println("... for patient " +patientId + " device " + deviceId + " running " + clientOs);
		return Response.ok().build();
	}

	/** Accessor, used in unit testing. */
	public EntityHome<Reading> getReadingBean() {
		return readingBean;
	}

	/** Accessor, used in unit testing. */
	public void setReadingBean(ReadingHome readingBean) {
		this.readingBean = readingBean;
	}

}