package com.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShellCommand extends Activity {
    private TextView output;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String commandOutput = runShellCommand();
        output= (TextView)findViewById(R.id.output);
        output.setText(commandOutput);
    }

	private String runShellCommand() {
		try {
			Process process = Runtime.getRuntime().exec("/system/bin/ps");
			InputStreamReader reader = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(reader);
		    int numRead;
		    char[] buffer = new char[5000];
		    StringBuffer commandOutput = new StringBuffer();
		    while ((numRead = bufferedReader.read(buffer)) > 0) {
		        commandOutput.append(buffer, 0, numRead);
		    }
		    bufferedReader.close();
		    process.waitFor();

		    return commandOutput.toString();
		} catch (IOException e) {

		    throw new RuntimeException(e);

		} catch (InterruptedException e) {

		    throw new RuntimeException(e);
		}

	}
}