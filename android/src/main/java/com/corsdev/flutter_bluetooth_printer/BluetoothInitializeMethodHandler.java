package com.corsdev.flutter_bluetooth_printer;

import static android.app.Activity.RESULT_OK;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class BluetoothInitializeMethodHandler extends MethodHandler implements PluginRegistry.ActivityResultListener {
    private static final int BLUETOOTH_INTENT_REQUEST = 100;

    private final BroadcastReceiver deviceDiscoveryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String requestedAddress = null;
                String requestedName = null;

                if (call.hasArgument("deviceAddress"))
                    requestedAddress = call.argument("deviceAddress");

                if (call.hasArgument("deviceAddress"))
                    requestedName = call.argument("deviceName");

                if (device.getAddress().equals(requestedAddress) || device.getName().equals(requestedName))
                    state.device = device;

                state.adapter.cancelDiscovery();

                connectToDevice();
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                activityBinding.getActivity().unregisterReceiver(this);
            }
        }
    };


    public BluetoothInitializeMethodHandler(ActivityPluginBinding activityBinding, @NonNull BluetoothState state, @NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        super(activityBinding, state, call, result);
    }

    @Override
    public void handle() {
        if (state.adapter == null) {
            result.error("9999", "Bluetooth not supported by device", null);
            return;
        }

        if ((!call.hasArgument("deviceName") || !(call.argument("deviceName") instanceof String)) && (!call.hasArgument("deviceAddress") || !(call.argument("deviceAddress") instanceof String))) {
            result.error("0002", "Device Name or Address Missing", null);
            return;
        }

        if (!call.hasArgument("uuid") || !(call.argument("uuid") instanceof String)) {
            result.error("0004", "UUID missing", null);
            return;
        }

        if (!state.adapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.activityBinding.getActivity().startActivityForResult(enableBluetoothIntent, BLUETOOTH_INTENT_REQUEST);
            this.activityBinding.addActivityResultListener(this);
        } else {
            findBluetoothDevice();
        }

    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != BLUETOOTH_INTENT_REQUEST)
            return false;

        this.activityBinding.removeActivityResultListener(this);

        if (resultCode == RESULT_OK)
            findBluetoothDevice();
        else
            this.result.error("0001", "Bluetooth Enable Request Cancelled", null);

        return true;
    }

    private void findBluetoothDevice() {

        boolean isPaired = false;

        if (call.hasArgument("isPaired") && call.argument("isPaired") instanceof Boolean) {
            isPaired = call.argument("isPaired");
        }

        String requestedAddress = null;
        String requestedName = null;

        if (call.hasArgument("deviceAddress"))
            requestedAddress = call.argument("deviceAddress");

        if (call.hasArgument("deviceAddress"))
            requestedName = call.argument("deviceName");

        if (isPaired) {
            this.state.device = this.getBluetoothDeviceFromPaired(requestedAddress, requestedName);
            connectToDevice();
        } else {
            this.state.adapter.startDiscovery();
            this.activityBinding.getActivity().registerReceiver(deviceDiscoveryReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            this.activityBinding.getActivity().registerReceiver(deviceDiscoveryReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        }

    }

    private void connectToDevice() {
        if (this.state.device == null) {
            this.result.error("0003", "Device not found", null);
            return;
        }

        try {
            this.state.socket = this.state.device.createRfcommSocketToServiceRecord(UUID.fromString(call.argument("uuid")));
            this.state.socket.connect();

            result.success(this.state.socket.isConnected());
        } catch (IOException exception) {
            exception.printStackTrace();
            result.error("0005", exception.getMessage(), null);
        }
    }

    private BluetoothDevice getBluetoothDeviceFromPaired(String deviceAddress, String deviceName) {

        Set<BluetoothDevice> pairedDevices = this.state.adapter.getBondedDevices();

        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(deviceAddress) || device.getName().equals(deviceName))
                return device;
        }

        return null;
    }
}
