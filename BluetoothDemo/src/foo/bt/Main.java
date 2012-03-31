package foo.bt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity {
	protected static final String TAG = "bluetoothdemo";
	int REQUEST_ENABLE_BT = 1;
	EditText main;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main = (EditText) findViewById(R.id.mainTextArea);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.string.app_name);
        
        // Hook up the Discover button to its handler
        Button discover = (Button) findViewById(R.id.discoverButton);
        discover.setOnClickListener(discoverButtonHandler);
        
        // Hook up the ArrayAdapter to the ListView
        ListView lv = (ListView) findViewById(R.id.pairedBtDevices);
        lv.setAdapter(mNewDevicesArrayAdapter);
        
        BluetoothAdapter BT = BluetoothAdapter.getDefaultAdapter();
        if (BT == null) {
        	String noDevMsg = "This device does not appear to have a Bluetooth adapter, sorry";
        	main.setText(noDevMsg);
        	Toast.makeText(this, noDevMsg, Toast.LENGTH_LONG).show();
        	return;
        }
        if (!BT.isEnabled()) {
        	// Ask user's permission to switch the Bluetooth adapter On. 
        	Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        	startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } 
    }
        
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode==REQUEST_ENABLE_BT) {
    		if (resultCode==Activity.RESULT_OK) {
    			BluetoothAdapter BT = BluetoothAdapter.getDefaultAdapter();
    			String address = BT.getAddress();
    			String name = BT.getName();
    			String connectedMsg = "BT is on; your device is "  + name + " : " + address;
    			main.setText(connectedMsg);
    			Toast.makeText(this, connectedMsg, Toast.LENGTH_LONG).show();
    			Button discoverButton = (Button) findViewById(R.id.discoverButton);
    			discoverButton.setOnClickListener(discoverButtonHandler);
    		} else {
    			Toast.makeText(this, "Failed to enable Bluetooth adapter!", Toast.LENGTH_LONG).show();
    		}
    	} else {
    		Toast.makeText(this, "Unknown RequestCode " + requestCode, Toast.LENGTH_LONG).show();
    	}
    }
    
    /** When the user clicks the Discover button, get the list of paired devices
     */
    OnClickListener discoverButtonHandler = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "in onClick(" + v + ")");
			// IntentFilter for found devices
			IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			// Broadcast receiver for any matching filter
			Main.this.registerReceiver(mReceiver, foundFilter);

			IntentFilter doneFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			Main.this.registerReceiver(mReceiver, doneFilter);
		}
	};
	
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
    
	/** Receiver for the BlueTooth Discovery Intent; put the paired devices
	 * into the viewable list.
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {    		
			@Override
    		public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				Log.d(TAG, "in onReceive, action = " + action);
    			
    			if (BluetoothDevice.ACTION_FOUND.equals(action)){
    				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    				
    				if(btDevice.getBondState() != BluetoothDevice.BOND_BONDED){
    					// XXX use a better type and a Layout in the visible list
    					mNewDevicesArrayAdapter.add(btDevice.getName()+"\n"+btDevice.getAddress());
    				}
    			}
    			else
    				if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
    					setProgressBarIndeterminateVisibility(false);
    					setTitle(R.string.select_device);
    					if (mNewDevicesArrayAdapter.getCount() == 0){
    						String noDevice = getResources().getText(R.string.none_paired).toString();
    						mNewDevicesArrayAdapter.add(noDevice);
    					}
    				}   			
    		}
    	};
}