import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:bloc/bloc.dart';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:meta/meta.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:weather/core/constants/constant.dart';
import 'package:weather/core/globals/globals.dart';
import 'package:weather/data/model/weather.dart';
import 'package:weather/domain/repositories/wetaher.dart';

part 'weather_event.dart';
part 'weather_state.dart';

class WeatherBloc extends Bloc<WeatherEvent, WeatherState> {
  WeatherBloc() : super(WeatherInitial(PageState())) {
    on<WeatherGetEvent>((event, emit) async {
      emit(WeatherLoadingState(state.pageState));

      Position position = await _determinePosition();
      WeatherRepository weatherRepository = WeatherRepository();
      await weatherRepository
          .getWeather(
          AppConstants.token, position.latitude, position.longitude)
          .then((value) async {
        if (value != null && value.list.isNotEmpty) {
          storeToLocal(value);

          emit(WeatherUpState(state.pageState.copyWith(
            weathers: value,
          )));
        } else {
          emit(WeatherLoadingState(state.pageState));

          Weathers weathers = await getWeatherFromStore();
          print(' svsdvsdvsdvsdv');
          print(weathers.list.first.main.tempMax);
          if (weathers.list.isNotEmpty) {
            final SnackBar snackBar =
            SnackBar(content: Text('Последние загруженные данные погоды'));
            snackbarKey.currentState?.showSnackBar(snackBar);
            emit(WeatherUpState(state.pageState.copyWith(
              weathers: weathers,
            )));

          } else {
            emit(WeatherErrorState(state.pageState.copyWith(
              error: 'Ошибка загрузки погоды',
            )));
          }
        }
      });


    });
  }

  Future<Position> _determinePosition() async {
    bool serviceEnabled;
    LocationPermission permission;

    // Test if location services are enabled.
    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      // Location services are not enabled don't continue
      // accessing the position and request users of the
      // App to enable the location services.
      return Future.error('Location services are disabled.');
    }

    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        // Permissions are denied, next time you could try
        // requesting permissions again (this is also where
        // Android's shouldShowRequestPermissionRationale
        // returned true. According to Android guidelines
        // your App should show an explanatory UI now.
        return Future.error('Location permissions are denied');
      }
    }

    if (permission == LocationPermission.deniedForever) {
      // Permissions are denied forever, handle appropriately.
      return Future.error(
          'Location permissions are permanently denied, we cannot request permissions.');
    }

    // When we reach here, permissions are granted and we can
    // continue accessing the position of the device.
    return await Geolocator.getCurrentPosition();
  }

  Future<void> storeToLocal(Weathers weathers) async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    String jsonNews = jsonEncode(weathers);
    sharedPreferences.setString('weathers', jsonNews);
  }

  Future<Weathers> getWeatherFromStore() async {
    print('getWeatherFromStore');
    SharedPreferences sharedNews = await SharedPreferences.getInstance();
    String newsFRomStore = sharedNews.getString('weathers') ?? '';
    Weathers weathers = Weathers.fromJson(jsonDecode(newsFRomStore));

    return (weathers.list.isNotEmpty ? weathers : const Weathers());
  }
}
