package com.example.soapdemo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SoapDemoActivity extends Activity {
	private static final String SOAP_ACTION = "http://tempuri.org/CelsiusToFahrenheit";
	private static final String METHOD_NAME = "CelsiusToFahrenheit";
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
	protected static final String TAG = "SoapDemo";
	private EditText degreesC, degreesF;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		degreesC = (EditText) findViewById(R.id.degreesC);
		degreesF = (EditText) findViewById(R.id.degreesF);
	}
	
	public void convert(View v) {
		Log.d(TAG, "Starting conversion thread.");
		new Thread() {
			public void run() {
				try {
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("Celsius", degreesC.getText().toString());
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					HttpTransportSE httpTransport = new HttpTransportSE(URL);
					httpTransport.debug = true;
					httpTransport.call(SOAP_ACTION, envelope);
					final SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
					runOnUiThread(new Runnable() {
						public void run() {
							degreesF.setText(result.toString());					
						}
					});
					Log.d(TAG, "Conversion finished, runOnUiThread started.");
                } catch (Exception e) {
                    Log.e(TAG, "Web Service Error", e);
                    Toast.makeText(SoapDemoActivity.this, "Web Service Error: " + e, Toast.LENGTH_LONG).show();
                }
			}
		}.start();
	}
}
