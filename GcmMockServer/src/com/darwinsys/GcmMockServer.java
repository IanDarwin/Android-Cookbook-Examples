package com.darwinsys;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * A very simple program which pretends to be a "server" in that it sends
 * a notification to the Google Cloud Messaging Server to cause it to send
 * a message to our GCM Client.
 * @author Ian Darwin, http://androidcookbook.com/
 */
public class GcmMockServer {
	/** Confidential Server API key gotten from the Google Dev Console -> Credentials -> Public API Access -> Key for Android Apps */
	final static String AUTH_KEY;
	
	final static String POST_URL = "https://android.googleapis.com/gcm/send";

	public static void main(String[] args) throws Exception {
		
		final  String[][]  MESSAGE_HEADERS = {
			{ "Content-Type", "application/json"},
			{ "Authorization", "key=" + AUTH_KEY}
		};

		String regIdFromClientApp = "APA91bGPM_7ll4k4-ZQNkNvVESIrNH8NclJV_EP5qFyEupDoWcgu-Ko2dkgWwJJvktuCMoDo-eMiWPEHRP7npr1KUuU38z9aojF0zJ6BhtVEZyzU47HrMbemTRbrC7vx_bbVyDzON9a4Y8wP6CcrGOsUtGCuiivRJcJC8qIl6mZ3636lKXOU16w";
		String jsonMessage = 
				"{\n" +
				"	\"registration_ids\" : [\""+ regIdFromClientApp + "\"],\n" +
				"	\"data\" : {\n" +
				"		\"message\": \"See your doctor ASAP!\"\n" +
				"	}\n" +
				"}\n";
		
		// Dump out the HTTP send for debugging
		for (String[] hed : MESSAGE_HEADERS) {
			System.out.println(hed[0] + "=>" + hed[1]);
		}
		System.out.println(jsonMessage);
		
		// Actually send it.
		sendMessage(POST_URL, MESSAGE_HEADERS, jsonMessage);
	}

	private static void sendMessage(String postUrl, String[][] messageHeaders,
			String jsonMessage) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(postUrl).openConnection();
		for (String[] h : messageHeaders) {
			conn.setRequestProperty(h[0], h[1]);
		}
        System.out.println("Connected to " + postUrl);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);       // ensure response always from server

        PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(conn.getOutputStream()));
        
        pw.print(jsonMessage);
        pw.close();
        
        System.out.println("Connection status code " + conn.getResponseCode());
	}
	
	/** Static initializer, just to load the API key so it doesn't appear in the commit history */
	static {
			InputStream is = null;
			try {
				
				is = GcmMockServer.class.getResourceAsStream("keys.properties");
				if (is == null) {
					throw new RuntimeException("could not open keys files, maybe copy keys.properties.sample to keys.properties in resources?");
				}
				Properties p = new Properties();
				p.load(is);
				AUTH_KEY = p.getProperty("GCM_API_KEY");
				if (AUTH_KEY == null) {
					String message = "Could not find GCM_API_KEY in props";
					throw new ExceptionInInitializerError(message);
				}
				
			} catch (Exception e) {
				String message = "Error loading properties: " + e;
				throw new ExceptionInInitializerError(message);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// What a useless exception
					}
				}
			}
	}
}
