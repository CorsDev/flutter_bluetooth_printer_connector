package com.corsdev.flutter_bluetooth_printer;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public abstract class MethodHandler {
    protected BluetoothState state;
    protected ActivityPluginBinding activityBinding;
    protected MethodCall call;
    protected MethodChannel.Result result;

    public MethodHandler(ActivityPluginBinding activityBinding, @NonNull BluetoothState state, @NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        this.state = state;
        this.activityBinding = activityBinding;
        this.call = call;
        this.result = result;
    }

    abstract void handle();

}
