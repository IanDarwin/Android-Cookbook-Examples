package foo.rebooter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * This activity tries to show two ways of rebooting a device,
 * the "official" but normally disallowed method (coded by ian)
 * and a hack that works by overloading the system (coded by Saket).
 */
public class Main extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button b1 = (Button) findViewById(R.id.bootNormalButton);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PowerManager pwrMgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
				Toast.makeText(Main.this, 
					"I'll try, but will most likely fail, to reboot", Toast.LENGTH_LONG).show();
				pwrMgr.reboot(null);
				Toast.makeText(Main.this, 
					"Even more unlikely: still running after reoot!", Toast.LENGTH_LONG).show();
			}
		});
		Button b2 = (Button) findViewById(R.id.bootHackButton);
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(Main.this, 
						"About to try rebooting by resource exhaustion", Toast.LENGTH_LONG).show();
				while (true) {
					Toast.makeText(Main.this, 
						"Hacking ur device", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}