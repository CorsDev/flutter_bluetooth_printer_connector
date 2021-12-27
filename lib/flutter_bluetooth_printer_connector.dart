import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class FlutterBluetoothPrinterConnector {
  static const MethodChannel _channel =
      MethodChannel('com.corsdev.flutter_bluetooth_printer');

  static Future<bool> initialize(
      {required String uuid,
      String? deviceName,
      String? deviceAddress,
      bool isPaired = false}) async {
    if (deviceName == null && deviceAddress == null) {
      throw Exception("Device Name And Address Missing");
    }

    return await _channel.invokeMethod("initialize", {
      "uuid": uuid,
      "deviceName": deviceName,
      "deviceAddress": deviceAddress,
      "isPaired": isPaired
    });
  }

  static Future<bool> get isConnected async {
    return await _channel.invokeMethod("isConnected");
  }

  static Future<bool> sendToPrinter(Uint8List data) async {
    return await _channel.invokeMethod("sendToPrinter", {"data": data});
  }

  static Future<bool> sendToPrinterList(List<int> data) async {
    return await FlutterBluetoothPrinterConnector.sendToPrinter(
        Uint8List.fromList(data));
  }

  static void close() async {
    await _channel.invokeMethod("close");
  }
}
