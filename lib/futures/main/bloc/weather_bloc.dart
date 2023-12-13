import 'dart:async';
import 'dart:developer';

import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';

part 'weather_event.dart';
part 'weather_state.dart';

class WeatherBloc extends Bloc<WeatherEvent, WeatherState> {
  WeatherBloc() : super(WeatherInitial()) {
    print(' kpmv');
    on<WeatherEvent>((event, emit) {
      on<WeatherGetEvent>((event, emit) {
        log(' pkwpk');

      });
    });
  }
}
