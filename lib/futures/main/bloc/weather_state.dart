part of 'weather_bloc.dart';

@immutable
abstract class WeatherState {
  final PageState pageState;

  const WeatherState(this.pageState);
}

class WeatherInitial extends WeatherState {
  const WeatherInitial(PageState pageState) : super(pageState);

}

class WeatherLoadingState extends WeatherState {
  const WeatherLoadingState(PageState pageState) : super(pageState);

}

class WeatherUpState extends WeatherState {
  const WeatherUpState(PageState pageState) : super(pageState);

}

class WeatherErrorState extends WeatherState {
  const WeatherErrorState(PageState pageState) : super(pageState);

}


class PageState {
  bool onAwait;
  String error;
  Weathers weathers;


  PageState({
    this.onAwait = false,
    this.error = '',
    this.weathers = const Weathers(),

  });

  PageState copyWith({
    bool? onAwait,
    String? error,
    Weathers? weathers,

  }) {
    return PageState(
      onAwait: onAwait ?? this.onAwait,
      error: error ?? this.error,
      weathers: weathers ?? this.weathers,

    );
  }
}

