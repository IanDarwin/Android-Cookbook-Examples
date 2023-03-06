package com.darwinsys.servicedemos;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class RemoteService extends IntentService {

    public RemoteService() {
        super("External Service Demo");
    }

    @Nullable @Override
    public IBinder onBind(Intent intent) {
        throw new IllegalStateException("Non-bind()able service got called at onBind!");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        int pid = android.os.Process.myPid();
        Toast.makeText(this, "Remote Service PID=" + pid, Toast.LENGTH_LONG).show();
    }
}
