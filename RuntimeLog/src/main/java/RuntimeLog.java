import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Run-time file-based logging, using standard java.util.logging.
 * It is REALLY too bad that JUL was added before Java enums!
 */
public class RuntimeLog {
	// The JUL Log Levels are:
	// SEVERE (highest value)
	// WARNING
	// INFO
	// CONFIG
	// FINE
	// FINER
	// FINEST (lowest value)

	// Change this to MODE_DEBUG to use for in-house debugging
	enum Mode {
		MODE_DEBUG,
		MODE_RELEASE
	}
	private static final Mode mode = Mode.MODE_RELEASE;
	private static String logfileName = "/sdcard/YourAppName.log";
	private static Logger logger;
	
	// initialize the log on first use of the class and 
	// create a custom log formatter 

	static {
		try {
			FileHandler fh = new FileHandler(logfileName, true);
			fh.setFormatter(new Formatter() {
				public String format(LogRecord rec) {
					java.util.Date date = new java.util.Date();
					return new StringBuffer(1000)
							.append((date.getYear())).append('/')
							.append(date.getMonth()).append('/')
							.append(date.getDate())
							.append(' ')
							.append(date.getHours())
							.append(':')
							.append(date.getMinutes()).append(':')
							.append(date.getSeconds())
							.append('\n')
							.toString();
				}
			});
			logger = Logger.getLogger(logfileName);
			logger.addHandler(fh);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// the log method
	public static void log(Level logLevel, String msg) {
		//don't log DEBUG and VERBOSE statements in release mode 
		if (mode == Mode.MODE_RELEASE && 
				logLevel.intValue() >= Level.FINE.intValue())
			return;
		final LogRecord record = new LogRecord(logLevel, msg);
		record.setLoggerName(logfileName);
		logger.log(record);
	}
	
	/** 
	 * Reveal the logfile path, so part of your app can read the 
	 * logfile and either email it to you, or
	 * upload it to your server via REST
	 * @return
	 */
	public static String getFileName() {
		return logfileName;
	}
}
