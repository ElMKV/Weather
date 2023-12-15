import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:hexcolor/hexcolor.dart';
import 'package:weather/core/constants/constant.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:weather/core/globals/globals.dart';
import 'package:weather/data/model/weather.dart';
import 'package:intl/intl.dart';

import 'package:weather/futures/main/bloc/weather_bloc.dart';
import 'package:intl/date_symbol_data_local.dart';



class HomePage extends StatelessWidget {
  const HomePage({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: BlocProvider(
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
                return Center(child: Text(state.pageState.error, style: TextStyle(color: Colors.white),));
              } else if (state is WeatherInitial) {
                return const SizedBox();
              }
              return SingleChildScrollView(
                child: Container(
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [
                        HexColor("#0700FF"),
                        HexColor("#000000"),
                      ],
                    ),
                  ),
                  child: Column(
                    children: [
                      SizedBox(height: 50,),
                      Padding(
                        padding: const EdgeInsets.only(left: 32.0),
                        child: Row(
                          children: [
                            SvgPicture.asset(
                              fit: BoxFit.scaleDown,
                              'assets/pin.svg',
                            ),
                            SizedBox(
                              width: 15,
                            ),
                            Text(
                              '${state.pageState.weathers.city.name}, ${state.pageState.weathers.city.country}',
                              style: TextStyle(fontSize: 20, color: Colors.white),
                            )
                          ],
                        ),
                      ),
                      SizedBox(
                        height: 30,
                      ),
                      getSvg(
                          state.pageState.weathers.list.first.weather.first.icon,
                          180,
                          180),
                      Text(
                        '${state.pageState.weathers.list.first.main.temp?.round()}℃',
                        style: TextStyle(
                            fontSize: 72,
                            color: Colors.white,
                            fontWeight: FontWeight.bold),
                      ),
                      Text(
                        '${state.pageState.weathers.list.first.weather.first.description}',
                        style: TextStyle(
                            fontSize: 24,
                            color: Colors.white,
                            fontWeight: FontWeight.normal),
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            'Макс: ${state.pageState.weathers.list.first.main.tempMax?.round()}℃ ',
                            style: TextStyle(
                                fontSize: 24,
                                color: Colors.white,
                                fontWeight: FontWeight.normal),
                          ),
                          Text(
                            'Мин: ${state.pageState.weathers.list.first.main.tempMin?.round()}℃',
                            style: TextStyle(
                                fontSize: 24,
                                color: Colors.white,
                                fontWeight: FontWeight.normal),
                          ),
                        ],
                      ),
                      SizedBox(
                        height: 30,
                      ),
                      Container(
                          height: 230,
                          width: 327,
                          decoration: BoxDecoration(
                              color: Colors.white10,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(20))),
                          child: Column(
                            children: [
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.all(16.0),
                                    child: Text('Сегодня',
                                        style: TextStyle(
                                            fontSize: 17,
                                            color: Colors.white,
                                            fontWeight: FontWeight.normal)),
                                  ),
                                  Padding(
                                    padding: const EdgeInsets.all(16.0),
                                    child: Text(
                                        DateFormat.MMMMd('ru_RU')
                                            .format(DateTime.now())
                                            .toString(),
                                        style: TextStyle(
                                            fontSize: 17,
                                            color: Colors.white,
                                            fontWeight: FontWeight.normal)),
                                  )
                                ],
                              ),
                              Divider(
                                color: Colors.white,
                                height: 1,
                              ),
                              SizedBox(
                                height: 173,
                                width: 327,
                                child: ListView.builder(
                                  scrollDirection: Axis.horizontal,
                                  shrinkWrap: true,
                                  itemCount: state.pageState.weathers.list.length,
                                  itemBuilder: (context, index) {
                                    int ts =
                                        state.pageState.weathers.list[index].dt!;
                                    int date = ts * 1000;
                                    DateTime dt =
                                        DateTime.fromMillisecondsSinceEpoch(date);
                                    String time = DateFormat('kk:mm').format(dt);

                                    return Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: SizedBox(
                                        width: 74,
                                        height: 142,
                                        child: Column(
                                          mainAxisAlignment:
                                              MainAxisAlignment.spaceAround,
                                          children: [
                                            Text(
                                              time,
                                              style: TextStyle(
                                                  color: Colors.white,
                                                  fontSize: 15),
                                            ),
                                            getSvg(
                                                state
                                                    .pageState
                                                    .weathers
                                                    .list[index]
                                                    .weather
                                                    .first
                                                    .icon,
                                                30,
                                                30),
                                            Text(
                                              '${state.pageState.weathers.list[index].main.temp?.round()}℃',
                                              style: TextStyle(
                                                  color: Colors.white,
                                                  fontSize: 15),
                                            ),
                                          ],
                                        ),
                                      ),
                                    );
                                  },
                                ),
                              ),
                            ],
                          )),
                      SizedBox(
                        height: 30,
                      ),
                      Container(
                          height: 115,
                          width: 327,
                          decoration: BoxDecoration(
                              color: Colors.white10,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(20))),
                          child: Column(
                            children: [
                              ListTile(
                                leading: SvgPicture.asset(
                                  'assets/Wind.svg',
                                  width: 24,
                                  height: 24,
                                ),
                                title: Text(
                                  '${state.pageState.weathers.list.first.wind.speed.round()} м/с',
                                  style: TextStyle(color: Colors.white12),
                                ),
                                trailing: Text(
                                  getCurrentWindSide(state.pageState.weathers.list.first.wind.deg),
                                  style: TextStyle(color: Colors.white),
                                ),
                              ),
                              ListTile(
                                leading: SvgPicture.asset(
                                  'assets/Drop.svg',
                                  width: 24,
                                  height: 24,
                                ),
                                title: Text(
                                  '${state.pageState.weathers.list.first.main.humidity?.round()}%',
                                  style: TextStyle(color: Colors.white12),
                                ),
                                trailing: Text(
                                  getCurrentHum(state.pageState.weathers.list.first.main.humidity ?? 0),
                                  style: TextStyle(color: Colors.white),
                                ),
                              ),
                            ],
                          )),
                      SizedBox(
                        height: 100,
                      ),

                      // Text(state.pageState.weathers.main.temp.toString())
                    ],
                  ),
                ),
              );
            },
          )),
    );
  }

  Widget getSvg(String main, double width, double height) {
    print(main);
    switch (main) {
      case '04n':
        return SvgPicture.asset(
          'assets/CloudMoon.svg',
          width: width,
          height: height,
        );
      case '04d':
        return SvgPicture.asset(
          width: width,
          height: height,
          'assets/CloudSun.svg',
        );
      case '13n':
        return SvgPicture.asset(
          width: width,
          height: height,
          'assets/CloudSnow.svg',
        );
      case '13d':
        return SvgPicture.asset(
          width: width,
          height: height,
          'assets/CloudSnow.svg',
        );
      case '10n':
        return SvgPicture.asset(
          width: width,
          height: height,
          'assets/CloudRain.svg',
        );
      case '10d':
        return SvgPicture.asset(
          width: width,
          height: height,
          'assets/CloudRain.svg',
        );
      default:
        {
          return SvgPicture.asset(
            width: width,
            height: height,
            'assets/CloudRain.svg',
          );
        }
    }
  }

  String getCurrentWindSide(int deg) {
    if (deg <= 90) {
      return 'Ветер северо-восточный';
    }
    else if (deg > 90 && deg <= 180) {
      return 'Ветер юго-восточный';
    }
    else if (deg > 180 && deg <= 270) {
      return 'Ветер юго-западный';
    }
    else if (deg > 270 && deg <= 360) {
      return 'Ветер северо-восточный';
    }
    else {
      return 'Нет данных';
    }

  }
  String getCurrentHum(double hum) {
    print(hum);
    if (hum <= 30) {
      return 'Низкая Влажность';
    }
    else if (hum <= 66) {
      return 'Средняя Влажность';
    }
    else if (hum <= 100) {
      return 'Высокая Влажность';
    }
    else {
      return 'Нет данных';
    }

  }
}