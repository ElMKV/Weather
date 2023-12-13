import 'package:flutter/material.dart';
import 'package:weather/core/constants/constant.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:weather/domain/repositories/wetaher.dart';
import 'package:weather/futures/main/bloc/weather_bloc.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: Scaffold(body: const HomePage()),
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => WeatherBloc()..add(WeatherGetEvent()),
      child: BlocConsumer<WeatherBloc, WeatherState>(
  listener: (context, state) {
    // TODO: implement listener
  },
  builder: (context, state) {
    return Text(' lofllf');
  },
)
    );

  }
}


