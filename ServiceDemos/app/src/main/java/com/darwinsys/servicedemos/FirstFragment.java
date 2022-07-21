package com.darwinsys.servicedemos;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.darwinsys.servicedemos.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private static final String TAG = "ServiceDemos";
    private FragmentFirstBinding binding;
    private boolean boundServiceBound = false;
    private BoundServiceDemo.MyBinder binder;
    private BoundServiceDemo boundService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonIntentService.setOnClickListener(v -> {
            var intent = new Intent(getContext(), IntentServiceDemo.class);
            intent.putExtra("message", "Hello Intentionally");
            intent.setData(Uri.parse("tel:555-1212"));
            getContext().startService(intent);
        });

        ServiceConnection sc = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d(TAG, "Bound Service Connected");
                binder = (BoundServiceDemo.MyBinder)iBinder;
                boundService = binder.getService();
                boundServiceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG, "Bound Service Disconnected");
                boundService = null;
                boundServiceBound = false;
            }
        };

        binding.buttonBindBoundService.setOnClickListener(v -> {
            Log.d(TAG, "buttonBoundService CLICKED");
            if (boundServiceBound) {
                return;
            }
            var intent = new Intent(getContext(), BoundServiceDemo.class);
            intent.putExtra("message", "Hello Intentionally");
            intent.setData(Uri.parse("tel:555-1212"));
            Log.d(TAG, "Binding to service: " + intent);
            var ret = getContext().bindService(intent, sc, Context.BIND_AUTO_CREATE);
            Log.d(TAG, ret ? "Service bound" : "Service NOT bound");
        });

        binding.buttonUseBoundService.setOnClickListener(v -> {
            Log.d(TAG, "buttonUseBoundService clicked");
            String message = "Service not bound!";
            if (boundServiceBound) {
                message = boundService.getMessage();
            }
            Log.d(TAG, message);
        });

        binding.buttonUnbindBoundService.setOnClickListener(v -> {
            if (boundServiceBound) {
                getContext().unbindService(sc);
            }
            Log.d(TAG, "Unbound");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
