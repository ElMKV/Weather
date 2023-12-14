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
        Locale('ru'), // English
      ],
      theme: ThemeData(
        useMaterial3: true,
      ),
      home:
          Scaffold(backgroundColor: Colors.blueAccent, body: const HomePage()),
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
              return SizedBox();
            }
            return Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.place, size: 35, color: Colors.white,),
                        Text(
                          '${state.pageState.weathers.name},',
                          style: TextStyle(
                              fontSize: 30,
                              color: Colors.white,
                              fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                    Text(
                      '${DateTime.now().day}, ${DateFormat('EEEE').format(DateTime.now())}',
                      style: TextStyle(
                          fontSize: 30,
                          color: Colors.white,
                          fontWeight: FontWeight.bold),
                    ),

                    Stack(
                      children: [
                        Text(
                          '${state.pageState.weathers.main.temp?.round()}°',
                          style: TextStyle(
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

                    // Text(
                    //   'Ощущается как',
                    //   style: TextStyle(fontSize: 25),
                    // ),
                    // Text(
                    //   '${state.pageState.weathers.main.feelsLike?.round()},',
                    //   style: TextStyle(fontSize: 25),
                    // ),
                    SizedBox(
                      height: 50,
                    ),
                    Center(
                      child: Container(
                        width: 300.0,
                        height: 100.0,
                        decoration: BoxDecoration(
                          color: Colors.blue[50],
                          borderRadius: BorderRadius.all(Radius.circular(60.0)),
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            Text(
                              'Min ${state.pageState.weathers.main.tempMin }',
                              style:
                                  TextStyle(fontSize: 20, color: Colors.blue,
                                      fontWeight: FontWeight.bold),
                            ),
                            Text(
                              'Max ${state.pageState.weathers.main.tempMax}',
                              style:
                                  TextStyle(fontSize: 20, color: Colors.blue,
                                      fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
                SizedBox(
                  height: 20,
                ),
              ],
            );
          },
        ));
  }
}
