import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class NativeServices {


  static const platform = MethodChannel('com.example.slice');

  static Future<Map<String, dynamic>?> getLocation() async {
    try {
      final location = await platform.invokeMethod('getLocation');

      print(location);
      return Map<String, dynamic>.from(location);
    } catch (e) {
      debugPrint('Native call failed>>>>>>>>>: $e');
      return null;
    }
  }

  static Future<void> startLocationService() async {
    try {
      debugPrint('start Location Service');
      await platform.invokeMethod('startLocationService');
      debugPrint('Location Service started###################');
    } catch (e) {
      debugPrint('Failed to start Location Service: $e ??????????????????');
    }
  }
}