import 'package:flutter/material.dart';
import 'package:weather/core/constants/constant.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:weather/data/model/weather.dart';
import 'package:intl/intl.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

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
      localizationsDelegates: [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: [
        const Locale('ru'), // English
      ],
      theme: ThemeData(
        useMaterial3: true,
      ),
      home:
          const Scaffold(backgroundColor: Colors.blueAccent, body: HomePage()),
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
            print(state);
            if (state is WeatherLoadingState) {
              return const Center(child: CircularProgressIndicator());
            } else if (state is WeatherErrorState) {
              return Center(child: Text(state.pageState.error));
            } else if (state is WeatherInitial) {
              return const SizedBox();
            }
            return ListView(
              children: [
                const SizedBox(height: 100,),
                Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Icon(
                          Icons.place,
                          size: 35,
                          color: Colors.white,
                        ),
                        Text(
                          '${state.pageState.weathers.name},',
                          style: const TextStyle(
                              fontSize: 30,
                              color: Colors.white,
                              fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                    Text(
                      '${DateTime.now().day}, ${DateFormat('EEEE').format(DateTime.now())}',
                      style: const TextStyle(
                          fontSize: 30,
                          color: Colors.white,
                          fontWeight: FontWeight.bold),
                    ),
                    Stack(
                      children: [
                        Text(
                          '${state.pageState.weathers.main.temp?.round()}°',
                          style: const TextStyle(
                              fontSize: 200,
                              fontWeight: FontWeight.bold,
                              color: Colors.white),
                        ),
                        Positioned(
                            top: 120,
                            left: 210,
                            child: Row(
                              children: [
                                Image.network(
                                  'https://openweathermap.org/img/wn/${state.pageState.weathers.weather.first.icon}@4x.png',
                                  errorBuilder: (BuildContext context,
                                      Object exception,
                                      StackTrace? stackTrace) {
                                    return const SizedBox();
                                  },
                                )
                              ],
                            ))
                      ],
                    ),
                    const SizedBox(
                      height: 50,
                    ),
                    Center(
                      child: Container(
                        width: 300.0,
                        height: 100.0,
                        decoration: BoxDecoration(
                          color: Colors.blue[50],
                          borderRadius: const BorderRadius.all(Radius.circular(60.0)),
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            Text(
                              'Min ${state.pageState.weathers.main.tempMin}',
                              style: const TextStyle(
                                  fontSize: 20,
                                  color: Colors.blue,
                                  fontWeight: FontWeight.bold),
                            ),
                            Text(
                              'Max ${state.pageState.weathers.main.tempMax}',
                              style: const TextStyle(
                                  fontSize: 20,
                                  color: Colors.blue,
                                  fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                )
              ],
            );
          },
        ));
  }
}
