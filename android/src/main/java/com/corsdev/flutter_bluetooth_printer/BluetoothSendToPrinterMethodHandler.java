package com.corsdev.flutter_bluetooth_printer;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class BluetoothSendToPrinterMethodHandler extends MethodHandler {
    public BluetoothSendToPrinterMethodHandler(ActivityPluginBinding activityBinding, @NonNull BluetoothState state, @NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        super(activityBinding, state, call, result);
    }

    @Override
    void handle() {
        if (state.socket == null) {
            result.error("0001", "Not initialized", null);
            return;
        }

        try {
            if (!state.socket.isConnected())
                state.socket.connect();

            OutputStream outputStream = state.socket.getOutputStream();

            if (call.hasArgument("data") && call.argument("data") instanceof byte[])
                outputStream.write(call.argument("data"));

            result.success(true);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            result.error("9999", ioException.getMessage(), null);
        }

    }
}
