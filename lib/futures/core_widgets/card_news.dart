import 'package:flutter/material.dart';

class CardNews extends StatelessWidget {
  const CardNews({this.imgUrl,this.title, Key? key}) : super(key: key);
  final String? imgUrl;
  final String? title;

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 10,
      shadowColor: Colors.black,
      color: Colors.white,
      child: SizedBox(
        width: 300,
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            children: [
              CircleAvatar(
                backgroundColor: Colors.grey,
                radius: 104,
                child: CircleAvatar(
                  backgroundImage: NetworkImage(
                      imgUrl.toString()), //NetworkImage
                  radius: 100,
                ), //CircleAvatar
              ), //CircleAvatar
              const SizedBox(
                height: 10,
              ), //SizedBox
              Center(
                child: Text(
                  title ?? '',
                  overflow: TextOverflow.ellipsis,

                  style: TextStyle(
                    fontSize: 16,
                    color: Colors.grey[900],
                    fontWeight: FontWeight.w300,
                  ), //Textstyle
                ),
              ), //Text
              const SizedBox(
                height: 10,
              ), //SizedBox//Text
              const SizedBox(
                height: 10,
              ), //SizedBox
            ],
          ), //Column
        ), //Padding
      ), //SizedBox
    );
  }
}

