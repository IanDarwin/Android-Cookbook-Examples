package com.darwinsys.soundrec;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * The Settings or Preferences activity.
 * This will be SOOO cleanable-uppable when Android catches up to Java 8
 * @author Ian Darwin
 */
public class SettingsActivity extends PreferenceActivity {

	static final String DIRECTORY_NAME = "soundrec";
	
	// Keys MUST agree with keys defined in settings.xml!
	
	static final String OPTION_DIR = "dir";
	static final String OPTION_SEEN_EULA = "accepted_eula";	// NOT IN GUI FOR OBVIOUS REASONS
	static final String OPTION_SEEN_WELCOME = "seen_welcome"; // Ditto
	//static final String OPTION_FORMAT = "format";
	static final String OPTION_OSM_USER = "osm_username";
	//static final String OPTION_OSM_PASS = "osm_password";
	private static String OPTION_ALWAYS_UPLOAD = "osm_alwaysUpload";
	private static String OPTION_USE_SANDBOX = "osm_useSandbox";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
	}
	
	/** No set method, it is set by our PreferencesActivity subclass */
	public static String getDirectory(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(OPTION_DIR, null);
	}
	
	/** No set method, it is set by our PreferencesActivity subclass */
	public static String getOSMUserName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(OPTION_OSM_USER, null);
	}
	
	/** No set method, it is set by our PreferencesActivity subclass */
	public static boolean isAlwaysUpload(Context context) {
		Getter t = new Getter(context, OPTION_ALWAYS_UPLOAD);
		ThreadUtils.executeAndWait(t);
		return t.getSeen();
	}

	public static void setSeenEula(final Context context, final boolean seenValue) {
		ThreadUtils.execute(new Setter(context, OPTION_SEEN_EULA, seenValue));
	}
	
	public static boolean hasSeenEula(final Context context) {		
		Getter t = new Getter(context, OPTION_SEEN_EULA);
		ThreadUtils.executeAndWait(t);
		return t.getSeen();
	}
	
	public static void setSeenWelcome(final Context context, final boolean seenValue) {
		ThreadUtils.execute(new Setter(context, OPTION_SEEN_WELCOME, seenValue));
	}
	
	public static boolean hasSeenWelcome(final Context context) {		
		Getter t = new Getter(context, OPTION_SEEN_WELCOME);
		ThreadUtils.executeAndWait(t);
		return t.getSeen();
	}
	
	public static boolean useSandbox(final Context context) {		
		Getter t = new Getter(context, OPTION_USE_SANDBOX);
		ThreadUtils.executeAndWait(t);
		return t.getSeen();
	}
	
	/** Q&D way to do gets on a background thread. */
	private static class Getter extends Thread {
		public Getter(Context context, String optionName) {
			super();
			this.context = context;
			this.optionName = optionName;
		}
		Context context;
		String optionName;
		boolean seen;
		
		@Override
		public void run() {
			seen = PreferenceManager.getDefaultSharedPreferences(context).
					getBoolean(optionName, false);
		}
		public boolean getSeen() {
			return seen;
		}
	}
	
	/** Q&D way to do sets on a background thread. */
	private static class Setter extends Thread {
		public Setter(Context context, String optionName, boolean seenValue) {
			super();
			this.context = context;
			this.optionName = optionName;
			this.seenValue = seenValue;
		}
		Context context;
		String optionName;
		boolean seenValue;
		@Override
		public void run() {
			PreferenceManager.getDefaultSharedPreferences(context).
			edit().putBoolean(optionName, seenValue).commit();
		}
	}


	


}
