package example.com.javatimedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.TemporalAdjusters;

public class MainActivity extends AppCompatActivity {

    TextView monthly, weekly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getApplication());
        setContentView(R.layout.activity_main);
        monthly = (TextView) findViewById(R.id.monthly);
        weekly = (TextView) findViewById(R.id.weekly);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weeklyPayDay =
                now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        weekly.setText("Weekly employees' payday is Friday " +
                weeklyPayDay.getMonth() + " " +
                weeklyPayDay.getDayOfMonth());
        LocalDateTime monthlyPayDay =
                now.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
        monthly.setText("Monthly employees are paid on " +
                monthlyPayDay.getMonth() + " " +
                monthlyPayDay.getDayOfMonth());
    }
}
