import 'dart:convert';
import 'dart:ffi';

import 'package:dio/dio.dart';
import 'package:weather/core/constants/constant.dart';

class WeatherApi {
  static Future<Response?> getWeather(String token, double lat, double lon) async {
    var dio = Dio();
    Response? news;
    try {
      news = await dio.get('${AppConstants.base}/data/2.5/forecast',
          queryParameters: {"appid": token ,'lat': lat, 'lon': lon, 'units' : 'metric', 'lang': 'ru'});

      print('STATUS ${news.statusMessage}');
      print('DATA ${news.data}');
      return news;
    } catch (e) {
      print(e);
      rethrow;
    }
  }
}
