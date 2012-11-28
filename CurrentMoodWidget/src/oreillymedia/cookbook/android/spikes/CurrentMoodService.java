package oreillymedia.cookbook.android.spikes;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class CurrentMoodService extends Service {
	public static final String UPDATEMOOD = "UpdateMood";
	public static final String CURRENTMOOD = "CurrentMood";
	
	private String currentMood;
	private LinkedList<String> moods;
	
	public CurrentMoodService(){
		this.moods = new LinkedList<String>();
		fillMoodsList();
	}

    private void fillMoodsList() {
    	this.moods.add(":)");
    	this.moods.add(":(");
    	this.moods.add(":D");
    	this.moods.add(":X");
    	this.moods.add(":S");
    	this.moods.add(";)");
    	
    	this.currentMood = ";)";
    }


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
		
        Log.i(CurrentMoodWidgetProvider.WIDGETTAG, "onStartCommand");

        updateMood(intent);
			
		stopSelf(startId);
		
		return START_STICKY;
	}

	private String getRandomMood() {
		Random r = new Random(Calendar.getInstance().getTimeInMillis());
		int pos = r.nextInt(moods.size());
		return moods.get(pos);
	}

	private void updateMood(Intent intent) {
        Log.i(CurrentMoodWidgetProvider.WIDGETTAG, "This is the intent " + intent);
        if (intent != null){
    		String requestedAction = intent.getAction();
            Log.i(CurrentMoodWidgetProvider.WIDGETTAG, "This is the action " + requestedAction);
    		if (requestedAction != null && requestedAction.equals(UPDATEMOOD)){
	            this.currentMood = getRandomMood();
	            	            	 
	            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

	            Log.i(CurrentMoodWidgetProvider.WIDGETTAG, "This is the currentMood " + currentMood + " to widget " + widgetId);

	            AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(this);
	            RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widgetlayout);
	            views.setTextViewText(R.id.widgetMood, currentMood);
	            appWidgetMan.updateAppWidget(widgetId, views);
	            
	            Log.i(CurrentMoodWidgetProvider.WIDGETTAG, "CurrentMood updated!");
    		}
        }
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
