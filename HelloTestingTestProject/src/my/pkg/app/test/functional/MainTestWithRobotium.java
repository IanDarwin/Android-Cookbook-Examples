package my.pkg.app.test.functional;

import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import my.pkg.app.Main;

public class MainTestWithRobotium extends ActivityInstrumentationTestCase2<Main> {
    private Solo solo;

    public MainTestWithRobotium() {
        super("my.pkg.app", Main.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testTextViewTextIsCorrect() {
        assertTrue(solo.searchText("Hello World!"));
    }

    public void testSayGoodByeButtonTextIsCorrect() {
        assertTrue(solo.searchButton("Say Good Bye!"));
    }

    public void testPressSayGoodByeButton() {
        solo.clickOnButton("Say Good Bye!");
        assertTrue(solo.searchText("Good Bye World!"));
    }
}