package com.darwinsys.starsurvey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        OnRatingBarChangeListener barChangeListener = new OnRatingBarChangeListener() {			
			@Override
			public void onRatingChanged(RatingBar rBar, float fRating, boolean fromUser) {
				int rating = (int) fRating;
				String message = null;
				switch(rating) {
				case 1: message = "Sorry you're really upset with us"; break;
				case 2: message = "Sorry you're not happy"; break;
				case 3: message = "Good enough is not good enough"; break;
				case 4: message = "Thanks, we're glad you liked it."; break;
				case 5: message = "Awesome - thanks!"; break;
				}
				Toast.makeText(Main.this, 
					message,
					Toast.LENGTH_SHORT).show();
			}
		};
		final RatingBar sBar = (RatingBar) findViewById(R.id.serviceBar);
		sBar.setOnRatingBarChangeListener(barChangeListener);
		final RatingBar pBar = (RatingBar) findViewById(R.id.priceBar);
		pBar.setOnRatingBarChangeListener(barChangeListener);
        
        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String message = String.format(
						"Final Answer: Price %.0f/%d, Service %.0f/%d%nThank you!",
						sBar.getRating(), sBar.getNumStars(),
						pBar.getRating(), pBar.getNumStars()
						);
				// Thank the user
				Toast.makeText(Main.this, 
						message,
						Toast.LENGTH_LONG).show();
				// And upload the numbers to a database, hopefully...
				
				// That's all for this Activity, hence this App.
				finish();
			}
		});
    }
}