package com.facebook.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;

public class Main extends Activity implements OnClickListener
	{
		private static final String tag = "Main";
		private LoginButton mLoginButton;
		private TextView mText;
		private Button mRequestButton;
		private Button mPostButton;
		private Button mDeleteButton;
		private Button mUploadButton;

		private Facebook mFacebook;
		private AsyncFacebookRunner mAsyncRunner;

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				Log.d(tag, getResources().getString(R.string.CREATING_VIEW));
				mFacebook = new Facebook(getResources().getString(R.string.FACEBOOK_ID_TEST));
				setContentView(R.layout.facebook_login_view);

				mLoginButton = (LoginButton) this.findViewById(R.id.login);
				mText = (TextView) Main.this.findViewById(R.id.txt);
				mRequestButton = (Button) findViewById(R.id.requestButton);
				mPostButton = (Button) findViewById(R.id.postButton);
				mDeleteButton = (Button) findViewById(R.id.deletePostButton);
				mUploadButton = (Button) findViewById(R.id.uploadButton);

				mFacebook.authorize(this, new DialogListener()
					{
						@Override
						public void onComplete(Bundle values)
							{
							}

						@Override
						public void onFacebookError(FacebookError error)
							{
							}

						@Override
						public void onError(DialogError e)
							{
							}

						@Override
						public void onCancel()
							{
							}
					});

				//
				mAsyncRunner = new AsyncFacebookRunner(mFacebook);

				SessionStore.restore(mFacebook, this);
				SessionEvents.addAuthListener(new SampleAuthListener());
				SessionEvents.addLogoutListener(new SampleLogoutListener());
				mLoginButton.init(this, mFacebook);

				mRequestButton.setOnClickListener(this);

				mRequestButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE : View.INVISIBLE);

				mUploadButton.setOnClickListener(this);

				mUploadButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE : View.INVISIBLE);

				mPostButton.setOnClickListener(this);

				mPostButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE : View.INVISIBLE);

			}
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data)
			{
				super.onActivityResult(requestCode, resultCode, data);
				mFacebook.authorizeCallback(requestCode, resultCode, data);
			}

		//
		public class SampleAuthListener implements AuthListener
			{

				@Override
				public void onAuthSucceed()
					{
						mText.setText("You have logged in! ");
						mRequestButton.setVisibility(View.VISIBLE);
						mUploadButton.setVisibility(View.VISIBLE);
						mPostButton.setVisibility(View.VISIBLE);
					}

				@Override
				public void onAuthFail(String error)
					{
						mText.setText("Login Failed: " + error);
					}
			}

		public class SampleLogoutListener implements LogoutListener
			{
				@Override
				public void onLogoutBegin()
					{
						mText.setText("Logging out...");
					}

				@Override
				public void onLogoutFinish()
					{
						mText.setText("You have logged out! ");
						mRequestButton.setVisibility(View.INVISIBLE);
						mUploadButton.setVisibility(View.INVISIBLE);
						mPostButton.setVisibility(View.INVISIBLE);
					}
			}

		public class SampleRequestListener extends BaseRequestListener
			{

				@Override
				public void onComplete(final String response, final Object state)
					{
						try
							{
								// process the response here: executed in background thread
								Log.d("Facebook-Example", "Response: " + response.toString());
								JSONObject json = Util.parseJson(response);
								final String name = json.getString("name");

								// then post the processed result back to the UI thread
								// if we do not do this, an runtime exception will be generated
								// e.g. "CalledFromWrongThreadException: Only the original
								// thread that created a view hierarchy can touch its views."
								Main.this.runOnUiThread(new Runnable()
									{
										@Override
										public void run()
											{
												mText.setText("Hello there, " + name + "!");
											}
									});
							}
						catch (JSONException e)
							{
								Log.w("Facebook-Example", "JSON Error in response");
							}
						catch (FacebookError e)
							{
								Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
							}
					}
			}

		public class SampleUploadListener extends BaseRequestListener
			{

				@Override
				public void onComplete(final String response, final Object state)
					{
						try
							{
								// process the response here: (executed in background thread)
								Log.d("Facebook-Example", "Response: " + response.toString());
								JSONObject json = Util.parseJson(response);
								final String src = json.getString("src");

								// then post the processed result back to the UI thread
								// if we do not do this, an runtime exception will be generated
								// e.g. "CalledFromWrongThreadException: Only the original
								// thread that created a view hierarchy can touch its views."
								Main.this.runOnUiThread(new Runnable()
									{
										@Override
										public void run()
											{
												mText.setText("Hello there, photo has been uploaded at \n" + src);
											}
									});
							}
						catch (JSONException e)
							{
								Log.w("Facebook-Example", "JSON Error in response");
							}
						catch (FacebookError e)
							{
								Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
							}
					}
			}

		public class WallPostRequestListener extends BaseRequestListener
			{

				@Override
				public void onComplete(final String response, final Object state)
					{
						Log.d("Facebook-Example", "Got response: " + response);
						String message = "<empty>";
						try
							{
								JSONObject json = Util.parseJson(response);
								message = json.getString("message");
							}
						catch (JSONException e)
							{
								Log.w("Facebook-Example", "JSON Error in response");
							}
						catch (FacebookError e)
							{
								Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
							}
						final String text = "Your Wall Post: " + message;
						Main.this.runOnUiThread(new Runnable()
							{
								@Override
								public void run()
									{
										mText.setText(text);
									}
							});
					}
			}

		public class WallPostDeleteListener extends BaseRequestListener
			{

				@Override
				public void onComplete(final String response, final Object state)
					{
						if (response.equals("true"))
							{
								Log.d("Facebook-Example", "Successfully deleted wall post");
								Main.this.runOnUiThread(new Runnable()
									{
										@Override
										public void run()
											{
												mDeleteButton.setVisibility(View.INVISIBLE);
												mText.setText("Deleted Wall Post");
											}
									});
							}
						else
							{
								Log.d("Facebook-Example", "Could not delete wall post");
							}
					}
			}

		public class SampleDialogListener extends BaseDialogListener
			{

				@Override
				public void onComplete(Bundle values)
					{
						final String postId = values.getString("post_id");
						if (postId != null)
							{
								Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
								mAsyncRunner.request(postId, new WallPostRequestListener());
								mDeleteButton.setOnClickListener(new OnClickListener()
									{
										@Override
										public void onClick(View v)
											{
												mAsyncRunner.request(postId, new Bundle(), "DELETE", new WallPostDeleteListener(), null);
											}
									});
								mDeleteButton.setVisibility(View.VISIBLE);
							}
						else
							{
								Log.d("Facebook-Example", "No wall post made");
							}
					}
			}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v)
			{

				if (v == mLoginButton)
					{

					}

				if (v == mRequestButton)
					{
						mAsyncRunner.request("me", new SampleRequestListener());
					}

				if (v == mUploadButton)
					{
						Bundle params = new Bundle();
						params.putString("method", "photos.upload");

						URL uploadFileUrl = null;
						try
							{
								uploadFileUrl = new URL("http://www.facebook.com/images/devsite/iphone_connect_btn.jpg");
							}
						catch (MalformedURLException e)
							{
								e.printStackTrace();
							}
						try
							{
								HttpURLConnection conn = (HttpURLConnection) uploadFileUrl.openConnection();
								conn.setDoInput(true);
								conn.connect();
								int length = conn.getContentLength();

								byte[] imgData = new byte[length];
								InputStream is = conn.getInputStream();
								is.read(imgData);
								params.putByteArray("picture", imgData);

							}
						catch (IOException e)
							{
								e.printStackTrace();
							}

						mAsyncRunner.request(null, params, "POST", new SampleUploadListener(), null);
					}

				if (v == mPostButton)
					{
						mFacebook.dialog(Main.this, "feed", new SampleDialogListener());
					}
				if (v == mDeleteButton)
					{

					}
			}
	}