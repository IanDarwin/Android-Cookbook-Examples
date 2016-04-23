public class MyActivity {

	public void onSend(View v) {

		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost req = new HttpPost(requestUri);
		req.addHeader("content-type", "application/json");

		// Base-64 encode username and password.
		// DO NOT USE THIS unless you're sure the protocol is HTTPS
		String userName = "SomeUser";
		String password = "don't use this";
		String authInfo = Base64.encodeToString(
			(userName + ":" + password).getBytes(), 0);
		request.addHeader("Authorization", authInfo);
		
		RequestHandler<String> handler = new BasicResponseHandler();
		String result = client.execute(request, handler);
	}
}

