package com.example.soapdemo;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SoapDemoActivity extends Activity {
	private static final String SOAP_ACTION = "http://tempuri.org/CelsiusToFahrenheit";
	private static final String METHOD_NAME = "CelsiusToFahrenheit";
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
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
		try {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("Celsius", degreesC.getText());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(URL);
			httpTransport.debug = true;
			httpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			degreesF.setText(result.toString());
		} catch (SoapFault e) {
			System.out.println("Soap Fault: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} catch (XmlPullParserException e) {
			System.out.println("XML Error: " + e.getMessage());
		}
	}
}
