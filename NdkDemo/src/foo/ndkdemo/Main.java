package foo.ndkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity {
	
	SqrtDemo d = new SqrtDemo();
	public double total = 0;
	
	enum Lang {
		C,
		Java
	}
	
	static {
		System.loadLibrary("sqrt-demo");
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button cb = (Button) findViewById(R.id.cButton);
        final Button jb = (Button) findViewById(R.id.jButton);
        OnClickListener handler = new OnClickListener() {			
			@Override
			public void onClick(View v) {
				final Lang lang = 
					jb == v ? Lang.Java : Lang.C;
				EditText tf = (EditText) findViewById(R.id.textArea);
				String s = tf.getText().toString();
				int numReps = Integer.parseInt(s);
				total = 0;
				long t0 = System.currentTimeMillis();
				for (int i = 0; i < numReps; i++) {
					switch(lang) {
					case C:
						total += SqrtDemo.sqrtC(123456789.0);
						break;
					case Java:
						total += SqrtDemo.sqrtJava(123456789.0);
						break;
					}
				}
				long t1 = System.currentTimeMillis();
				long time = t1 - t0;
				EditText log = (EditText) findViewById(R.id.resultLog);
				log.append(String.format("Time %d iters in %s was %d msec, avg result %f%n", numReps, lang, time, total / numReps));
			}
		};
		cb.setOnClickListener(handler);
		jb.setOnClickListener(handler);
    }
}