package my.pkg.app.test.functional;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.TextView;
import my.pkg.app.Main;
import my.pkg.app.R;

public class MainTestWithoutRobotium extends ActivityInstrumentationTestCase2<Main> {
    private Activity activity;
    public TextView textView;
    public Button button;

    public MainTestWithoutRobotium() {
        super("my.pkg.app", Main.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();
        assertNotNull(activity);
        button = (Button) activity.findViewById(R.id.button);
        assertNotNull(button);
        textView = (TextView) activity.findViewById(R.id.textView);
        assertNotNull(textView);
    }

    public void testTextViewTextIsCorrect() {
        assertEquals("Hello World!", textView.getText());
    }

    public void testSayGoodByeButtonTextIsCorrect() {
        assertEquals("Say Good Bye!", button.getText());
    }

    @UiThreadTest
    public void testPressSayGoodByeButton() {
        button.performClick();
        assertEquals("Good Bye World!", textView.getText());
    }
}