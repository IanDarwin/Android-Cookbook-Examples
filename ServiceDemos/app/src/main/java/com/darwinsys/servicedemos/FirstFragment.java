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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.darwinsys.servicedemos.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private static final String TAG = "ServiceDemos";
    private FragmentFirstBinding binding;
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
            Toast.makeText(getActivity(),  "Intent Service Started", Toast.LENGTH_SHORT).show();
        });

        ServiceConnection sc = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Toast.makeText(getActivity(),  "Bound Service Connected", Toast.LENGTH_SHORT).show();
                binder = (BoundServiceDemo.MyBinder)iBinder;
                boundService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Toast.makeText(getActivity(), "Bound Service Disconnected", Toast.LENGTH_SHORT).show();
                boundService = null;
            }
        };

        binding.buttonBindBoundService.setOnClickListener(v -> {
            if (boundService!= null) {
                Toast.makeText(getActivity(), "BoundService already bound!", Toast.LENGTH_SHORT).show();
                return;
            }
            var intent = new Intent(getContext(), BoundServiceDemo.class);
            intent.putExtra("message", "Hello Intentionally");
            intent.setData(Uri.parse("tel:555-1212"));
            var ret = getContext().bindService(intent, sc, Context.BIND_AUTO_CREATE);
            Toast.makeText(getActivity(), ret ? "Service bound" : "Service NOT bound", Toast.LENGTH_SHORT).show();
        });

        binding.buttonUseBoundService.setOnClickListener(v -> {
            if (boundService != null) {
                var message = boundService.getMessage();
                Toast.makeText(getActivity(), "BoundService returned " + message, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getActivity(), "Not bound, can't invoke", Toast.LENGTH_SHORT).show();
        });

        binding.buttonUnbindBoundService.setOnClickListener(v -> {
            if (boundService!= null) {
                getContext().unbindService(sc);
                boundService = null;
                Toast.makeText(getActivity(), "BoundService unbound ", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getActivity(), "Not bound, can't unbind", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
