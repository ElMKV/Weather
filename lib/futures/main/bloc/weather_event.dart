part of 'weather_bloc.dart';

@immutable
abstract class WeatherEvent {}

class WeatherGetEvent extends WeatherEvent {
  WeatherGetEvent();
}