package com.corsdev.flutter_bluetooth_printer;

import android.app.Activity;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterBluetoothPrinterPlugin
 */
public class FlutterBluetoothPrinterPlugin implements FlutterPlugin, ActivityAware {
    private BluetoothPrinterMethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new BluetoothPrinterMethodChannel(flutterPluginBinding);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        channel.setActivityBinding(binding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        channel.setActivityBinding(null);
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        channel.setActivityBinding(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        channel.setActivityBinding(null);
    }
}
