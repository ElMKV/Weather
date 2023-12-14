import 'dart:ffi';

import 'package:json_annotation/json_annotation.dart';

part 'weather.g.dart';

@JsonSerializable()
class Weathers {
  @JsonKey(name: 'coord')
  final Coord coord;
  @JsonKey(name: 'weather')
  final List<Weather> weather;
  @JsonKey(name: 'main')
  final Main main;
  @JsonKey(name: 'name')
  final String name;

  const Weathers({
    this.coord = const Coord(),
    this.weather = const [],
    this.main = const Main(),
    this.name = 'Нет данных',
  });

  factory Weathers.fromJson(Map<String, dynamic> json) =>
      _$WeathersFromJson(json);

  Map<String, dynamic> toJson() => _$WeathersToJson(this);

  Weathers copyWith({
    Coord? coord,
    List<Weather>? weather,
    Main? main,
    String? name,
  }) {
    return Weathers(
      coord: coord ?? this.coord,
      weather: weather ?? this.weather,
      main: main ?? this.main,
      name: name ?? this.name,
    );
  }
}

@JsonSerializable()
class Coord {
  @JsonKey(name: 'lon')
  final double lon;
  @JsonKey(name: 'lat')
  final double lat;

  const Coord({
    this.lon = 0,
    this.lat = 0,
  });

  factory Coord.fromJson(Map<String, dynamic> json) => _$CoordFromJson(json);

  Map<String, dynamic> toJson() => _$CoordToJson(this);

  Coord copyWith({
    double? lon,
    double? lat,
  }) {
    return Coord(
      lon: lon ?? this.lon,
      lat: lat ?? this.lat,
    );
  }
}

@JsonSerializable()
class Weather {
  @JsonKey(name: 'id')
  final int id;
  @JsonKey(name: 'main')
  final String main;
  @JsonKey(name: 'description')
  final String description;
  @JsonKey(name: 'icon')
  final String icon;

  const Weather({
    this.id = 0,
    this.main = '',
    this.description = '',
    this.icon = '',
  });

  factory Weather.fromJson(Map<String, dynamic> json) =>
      _$WeatherFromJson(json);

  Map<String, dynamic> toJson() => _$WeatherToJson(this);

  Weather copyWith({
    int? id,
    String? main,
    String? description,
    String? icon,
  }) {
    return Weather(
      id: id ?? this.id,
      main: main ?? this.main,
      description: description ?? this.description,
      icon: icon ?? this.icon,
    );
  }
}

@JsonSerializable()
class Main {
  @JsonKey(name: 'temp')
  final double? temp;
  @JsonKey(name: 'feels_like')
  final double? feelsLike;
  @JsonKey(name: 'temp_min')
  final double? tempMin;
  @JsonKey(name: 'temp_max')
  final double? tempMax;

  const Main({
    this.temp,
    this.feelsLike,
    this.tempMin,
    this.tempMax,
  });

  factory Main.fromJson(Map<String, dynamic> json) => _$MainFromJson(json);

  Map<String, dynamic> toJson() => _$MainToJson(this);

  Main copyWith({
    double? temp,
    double? feelsLike,
    double? tempMin,
    double? tempMax,
  }) {
    return Main(
      temp: temp ?? this.temp,
      feelsLike: feelsLike ?? this.feelsLike,
      tempMin: tempMin ?? this.tempMin,
      tempMax: tempMax ?? this.tempMax,
    );
  }
}
