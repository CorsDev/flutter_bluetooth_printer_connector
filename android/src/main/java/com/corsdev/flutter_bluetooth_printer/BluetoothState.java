package com.corsdev.flutter_bluetooth_printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothState {
    public BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    public BluetoothDevice device = null;
    public BluetoothSocket socket = null;
}
