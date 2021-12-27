import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_bluetooth_printer_connector/flutter_bluetooth_printer_connector.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  void printSomething() async {
    FlutterBluetoothPrinterConnector.sendToPrinterList(utf8.encode("This is something"));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: FutureBuilder<bool>(
          future: FlutterBluetoothPrinterConnector.initialize(
              uuid: "09bd778c-6767-11ec-90d6-0242ac120003",
              deviceName: "IposPrinter",
              isPaired: true),
          builder: (BuildContext context, AsyncSnapshot snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(
                child: CircularProgressIndicator(),
              );
            }

            if (snapshot.hasError) {
              return Center(
                child: Text(
                  snapshot.error!.toString(),
                  textAlign: TextAlign.center,
                ),
              );
            }

            if (!snapshot.data) {
              return const Center(
                child: Text(
                  "Couldn't connect",
                  textAlign: TextAlign.center,
                ),
              );
            }

            return Center(
              child: ElevatedButton(
                  onPressed: printSomething,
                  child: const Text("Print Something")),
            );
          },
        ),
      ),
    );
  }
}
