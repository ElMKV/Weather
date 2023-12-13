import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:weather/core/globals/globals.dart';
import 'package:weather/data/api/weather/weather_api.dart';
import 'package:weather/data/model/weather.dart';
import 'package:weather/data/network/dio_exception.dart';

class WeatherRepository {
  Future<Weathers?> getWeather(String token, double lat, double lon) async {
    try {
      final response = await WeatherApi.getWeather(token, lat, lon);
      return (response?.data != null)
          ? Weathers.fromJson(response!.data)
          : const Weathers();
    } on DioError catch (e) {
      final errorMessage = DioExceptions.fromDioError(e).toString();
      final SnackBar snackBar = SnackBar(content: Text(errorMessage));
      snackbarKey.currentState?.showSnackBar(snackBar);
      return const Weathers();
    }
  }
}


