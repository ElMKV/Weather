import 'dart:ffi';

import 'package:json_annotation/json_annotation.dart';

part 'weather.g.dart';


@JsonSerializable()
class Weathers {
  @JsonKey(name: 'list')
  final List<Listes> list;
  @JsonKey(name: 'city')
  final City city;


  const Weathers({
    this.list = const [],
    this.city = const City(),

  });

  factory Weathers.fromJson(Map<String, dynamic> json) =>
      _$WeathersFromJson(json);

  Map<String, dynamic> toJson() => _$WeathersToJson(this);

  Weathers copyWith({
    List<Listes>? list,
    City? city,
  }) {
    return Weathers(
      list: list ?? this.list,
      city: city ?? this.city,

    );
  }
}

@JsonSerializable()
class Listes {
  @JsonKey(name: 'dt')
  final int? dt;
  @JsonKey(name: 'main')
  final Main main;
  @JsonKey(name: 'weather')
  final List<Weather> weather;
  @JsonKey(name: 'wind')
  final Wind wind;

  const Listes({
    this.dt,
    this.main = const Main(),
    this.weather = const [],
    this.wind = const Wind(),
  });

  factory Listes.fromJson(Map<String, dynamic> json) => _$ListesFromJson(json);

  Map<String, dynamic> toJson() => _$ListesToJson(this);

  Listes copyWith({
    int? dt,
    Main? main,
    List<Weather>? weather,
    Wind? wind,
  }) {
    return Listes(
      dt: dt ?? this.dt,
      main: main ?? this.main,
      weather: weather ?? this.weather,
    );
  }
}

@JsonSerializable()
class City {
  @JsonKey(name: 'name')
  final String name;
  @JsonKey(name: 'country')
  final String country;

  const City({
    this.name = '',
    this.country = '',
  });

  factory City.fromJson(Map<String, dynamic> json) => _$CityFromJson(json);

  Map<String, dynamic> toJson() => _$CityToJson(this);

  City copyWith({
    String? name,
    String? country,
  }) {
    return City(
      name: name ?? this.name,
      country: country ?? this.country,
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
  @JsonKey(name: 'humidity')
  final double? humidity;

  const Main({
    this.temp,
    this.feelsLike,
    this.tempMin,
    this.tempMax,
    this.humidity,
  });

  factory Main.fromJson(Map<String, dynamic> json) => _$MainFromJson(json);

  Map<String, dynamic> toJson() => _$MainToJson(this);

  Main copyWith({
    double? temp,
    double? feelsLike,
    double? tempMin,
    double? tempMax,
    double? humidity,
  }) {
    return Main(
      temp: temp ?? this.temp,
      feelsLike: feelsLike ?? this.feelsLike,
      tempMin: tempMin ?? this.tempMin,
      tempMax: tempMax ?? this.tempMax,
      humidity: humidity ?? this.humidity,
    );
  }
}

@JsonSerializable()
class Wind {
  @JsonKey(name: 'speed')
  final double speed;
  @JsonKey(name: 'deg')
  final int deg;

  const Wind({
    this.speed = 0,
    this.deg = 0,

  });

  factory Wind.fromJson(Map<String, dynamic> json) => _$WindFromJson(json);

  Map<String, dynamic> toJson() => _$WindToJson(this);

  Wind copyWith({
    double? speed,
    int? deg,

  }) {
    return Wind(
      speed: speed ?? this.speed,
      deg: deg ?? this.deg,

    );
  }
}
