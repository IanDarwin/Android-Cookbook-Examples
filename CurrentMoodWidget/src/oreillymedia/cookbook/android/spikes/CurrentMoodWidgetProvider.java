package oreillymedia.cookbook.android.spikes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class CurrentMoodWidgetProvider extends AppWidgetProvider {
	public static final String WIDGETTAG = "WidgetMood";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
	    Log.i(WIDGETTAG, "onUpdate");
		
		final int N = appWidgetIds.length;
		
		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
		    int appWidgetId = appWidgetIds[i];
		    		    
		    Log.i(WIDGETTAG, "updating widget[id] " + appWidgetId);

		    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
		    
		    Intent intent = new Intent(context, CurrentMoodService.class);
		    intent.setAction(CurrentMoodService.UPDATEMOOD);
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		
		    views.setOnClickPendingIntent(R.id.widgetBtn, pendingIntent);
		    Log.i(WIDGETTAG, "pending intent set");
		    
		    // Tell the AppWidgetManager to perform an update on the current App Widget
		    appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}	
}
