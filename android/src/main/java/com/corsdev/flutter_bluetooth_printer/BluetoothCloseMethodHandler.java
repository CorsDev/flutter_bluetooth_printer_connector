package com.corsdev.flutter_bluetooth_printer;

import androidx.annotation.NonNull;

import java.io.IOException;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class BluetoothCloseMethodHandler extends MethodHandler {
    public BluetoothCloseMethodHandler(ActivityPluginBinding activityBinding, @NonNull BluetoothState state, @NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        super(activityBinding, state, call, result);
    }

    @Override
    void handle() {

        try {
            if (state.socket.isConnected())
                state.socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        state.socket = null;
        state.device = null;

    }
}
