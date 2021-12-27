package com.corsdev.flutter_bluetooth_printer;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class BluetoothIsConnectedMethodHandler extends MethodHandler{
    public BluetoothIsConnectedMethodHandler(ActivityPluginBinding activityBinding, @NonNull BluetoothState state, @NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        super(activityBinding, state, call, result);
    }

    @Override
    void handle() {
        result.success(state.socket != null && state.socket.isConnected());
    }
}
