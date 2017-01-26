package nl.codestone.cookbook.incomingcallinterceptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class IncomingCallInterceptor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {                                         // 1
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);                         // 2
        String msg = "Phone state changed to " + state;
        
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {                                   // 3
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);  // 4
            msg += ". Incoming number is " + incomingNumber;
            
            // TODO This would be a good place to "Do something when the phone rings" ;-)
            
        }
        
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }

}