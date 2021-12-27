package com.corsdev.flutter_bluetooth_printer;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class BluetoothPrinterMethodChannel extends MethodChannel implements MethodChannel.MethodCallHandler {
    private static String CHANNEL_NAME = "com.corsdev.flutter_bluetooth_printer";
    private ActivityPluginBinding activityBinding = null;
    private BluetoothState state;

    public BluetoothPrinterMethodChannel(@NonNull FlutterPlugin.FlutterPluginBinding flutterEngine) {
        super(flutterEngine.getBinaryMessenger(), CHANNEL_NAME);
        state = new BluetoothState();
        this.setMethodCallHandler(this);
    }

    public void setActivityBinding(ActivityPluginBinding activity) {
        this.activityBinding = activity;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (activityBinding == null) {
            result.error("9999", "Activity Not Attached", null);
            return;
        }

        MethodHandler handler = null;

        switch (call.method) {
            case "initialize":
                handler = new BluetoothInitializeMethodHandler(activityBinding, state, call, result);
                break;
            case "isConnected":
                handler = new BluetoothIsConnectedMethodHandler(activityBinding, state, call, result);
                break;
            case "sendToPrinter":
                handler = new BluetoothSendToPrinterMethodHandler(activityBinding, state, call, result);
                break;
            case "close":
                handler = new BluetoothCloseMethodHandler(activityBinding, state, call, result);
        }

        if (handler == null) {
            result.notImplemented();
            return;
        }

        handler.handle();
    }
}
