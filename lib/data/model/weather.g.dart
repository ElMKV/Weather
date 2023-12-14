// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'weather.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Weathers _$WeathersFromJson(Map<String, dynamic> json) => Weathers(
      list: (json['list'] as List<dynamic>?)
              ?.map((e) => Listes.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      city: json['city'] == null
          ? const City()
          : City.fromJson(json['city'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$WeathersToJson(Weathers instance) => <String, dynamic>{
      'list': instance.list,
      'city': instance.city,
    };

Listes _$ListesFromJson(Map<String, dynamic> json) => Listes(
      dt: json['dt'] as int?,
      main: json['main'] == null
          ? const Main()
          : Main.fromJson(json['main'] as Map<String, dynamic>),
      weather: (json['weather'] as List<dynamic>?)
              ?.map((e) => Weather.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      wind: json['wind'] == null
          ? const Wind()
          : Wind.fromJson(json['wind'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$ListesToJson(Listes instance) => <String, dynamic>{
      'dt': instance.dt,
      'main': instance.main,
      'weather': instance.weather,
      'wind': instance.wind,
    };

City _$CityFromJson(Map<String, dynamic> json) => City(
      name: json['name'] as String? ?? '',
      country: json['country'] as String? ?? '',
    );

Map<String, dynamic> _$CityToJson(City instance) => <String, dynamic>{
      'name': instance.name,
      'country': instance.country,
    };

Weather _$WeatherFromJson(Map<String, dynamic> json) => Weather(
      id: json['id'] as int? ?? 0,
      main: json['main'] as String? ?? '',
      description: json['description'] as String? ?? '',
      icon: json['icon'] as String? ?? '',
    );

Map<String, dynamic> _$WeatherToJson(Weather instance) => <String, dynamic>{
      'id': instance.id,
      'main': instance.main,
      'description': instance.description,
      'icon': instance.icon,
    };

Main _$MainFromJson(Map<String, dynamic> json) => Main(
      temp: (json['temp'] as num?)?.toDouble(),
      feelsLike: (json['feels_like'] as num?)?.toDouble(),
      tempMin: (json['temp_min'] as num?)?.toDouble(),
      tempMax: (json['temp_max'] as num?)?.toDouble(),
      humidity: (json['humidity'] as num?)?.toDouble(),
    );

Map<String, dynamic> _$MainToJson(Main instance) => <String, dynamic>{
      'temp': instance.temp,
      'feels_like': instance.feelsLike,
      'temp_min': instance.tempMin,
      'temp_max': instance.tempMax,
      'humidity': instance.humidity,
    };

Wind _$WindFromJson(Map<String, dynamic> json) => Wind(
      speed: (json['speed'] as num?)?.toDouble() ?? 0,
      deg: json['deg'] as int? ?? 0,
    );

Map<String, dynamic> _$WindToJson(Wind instance) => <String, dynamic>{
      'speed': instance.speed,
      'deg': instance.deg,
    };
