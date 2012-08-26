package com.example.epochjscalendar;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CalendarViewActivity extends Activity {

        private static final String tag = "CalendarViewActivity";
        private ImageView calendarToJournalButton;
        private Button calendarDateButton;
        private WebView webview;
        private Date selectedCalDate;

        private final Handler jsHandler = new Handler();

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
                Log.d(tag, "Creating View ...");
                super.onCreate(savedInstanceState);

                // Set the View Layer
                Log.d(tag, "Setting-up the View Layer");
                setContentView(R.layout.calendar_view);

                // Go to CreateJournalEntry
                calendarToJournalButton = 
                	(ImageView) this.findViewById(R.id.calendarToJournalButton);
                calendarToJournalButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Log.d(tag, "Re-directing -&gt; CreateEntryScreen ...");
                                Intent intent = 
                                    new Intent(getApplicationContext(), 
                                        CreateEntryScreen.class);
                                startActivity(intent);
                            }
                    });

                // User-Selected Calendar Date
                calendarDateButton = (Button) this.findViewById(R.id.calendarDateButton);

                // Get access to the WebView holder
                webview = (WebView) this.findViewById(R.id.webview);

                // Get the settings
                WebSettings settings = webview.getSettings();

                // Enable JavaScript
                settings.setJavaScriptEnabled(true);

                // Enable ZoomControls visibility
                settings.setSupportZoom(true);

                // Add JavaScript Interface
                webview.addJavascriptInterface(new MyJavaScriptInterface(), "android");
                
                // Set the Chrome Client
                webview.setWebChromeClient(new MyWebChromeClient());

                // Load the URL of the HTML file
                webview.loadUrl("file:///android_asset/calendarview.html");

            }

        public void setCalendarButton(Date selectedCalDate) {
                Log.d(tag, jsHandler.obtainMessage().toString());
                calendarDateButton.setText(
                    DateUtils.convertDateToSectionHeaderFormat(selectedCalDate.getTime()));
        }

        /**
         * 
         * @param selectedCalDate
         */
        public void setSelectedCalDate(Date selectedCalDate) {
                this.selectedCalDate = selectedCalDate;
            }

        /**
         * 
         * @return
         */
        public Date getSelectedCalDate()
            {
                return selectedCalDate;
            }

        /**
         * JAVA-&gt;JAVASCRIPT INTERFACE
         * 
         * @author wagied
         * 
         */
        final class MyJavaScriptInterface
            {
                private Date jsSelectedDate;
                MyJavaScriptInterface()
                    {
                        // EMPTY;
                    }

                public void onDayClick()
                    {
                        jsHandler.post(new Runnable()
                            {
                                public void run()
                                    {
                                        // Java telling JavaScript to do things
                                        webview.loadUrl("javascript: popup();");
                                    }
                            });
                    }

                /**
                 * NOTE: THIS FUNCTION IS BEING SET IN JAVASCRIPT User-selected Date in
                 * WebView
                 * 
                 * @param dateStr
                 */
                public void setSelectedDate(String dateStr)
                    {
                        Toast.makeText(getApplicationContext(), dateStr, 
                            Toast.LENGTH_SHORT).show();
                        Log.d(tag, "User Selected Date: JavaScript -&gt; Java : " + dateStr);

                        // Set the User Selected Calendar date
                        setJsSelectedDate(new Date(Date.parse(dateStr)));
                        Log.d(tag, "java.util.Date Object: " + 
                            Date.parse(dateStr));
                    }
                private void setJsSelectedDate(Date userSelectedDate)
                    {
                        jsSelectedDate = userSelectedDate;
                    }
                public Date getJsSelectedDate()
                    {
                        return jsSelectedDate;
                    }
            }

        /**
         * Alert pop-up for debugging purposes
         * 
         * @author wdavid01
         * 
         */
        final class MyWebChromeClient extends WebChromeClient
            {
                @Override
                public boolean onJsAlert(WebView view, String url, 
                    String message, JsResult result)
                    {
                        Log.d(tag, message);
                        result.confirm();
                        return true;
                    }
            }

        @Override
        public void onDestroy()
            {
                Log.d(tag, "Destroying View!");
                super.onDestroy();
            }
    }
