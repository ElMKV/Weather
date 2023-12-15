import 'dart:io';

import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:weather/firebase/storage.dart';
import 'package:weather/widgets/file_widget.dart';


class StoragePage extends StatefulWidget {
  const StoragePage({Key? key}) : super(key: key);

  @override
  State<StoragePage> createState() => _State();
}

class _State extends State<StoragePage> {
  bool _uploadRunning = false;
  String _buttonCaption = "Select file to upload";
  UploadTask? _task;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: const Center(child: Text("Firebase Storage"))),
        body: Padding(
            padding: const EdgeInsets.all(10.0),
            child: ListView(children: [
              Card(
                  color: Colors.grey.shade300,
                  child: Column(children: [
                    Container(height: 10),
                    const Padding(
                        padding: EdgeInsets.symmetric(horizontal: 10),
                        child: Text("Upload a file to storage",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                                fontSize: 16, fontWeight: FontWeight.bold))),
                    Container(height: 10),
                    Row(mainAxisAlignment: MainAxisAlignment.center, children: [

                      Container(width: 10),
                      if (_uploadRunning && _task != null)
                        IconButton(
                            onPressed: () async {
                              await _task!.cancel();
                              _uploadRunning = false;
                              _buttonCaption = "Select file to upload";
                              setState(() {});
                            },
                            icon: const Icon(Icons.stop_circle_outlined,
                                size: 36, color: Colors.blueAccent))
                    ]),
                    Container(height: 10)
                  ])),
              Container(height: 10),
              FutureBuilder(
                  future: Storage.getData(),
                  initialData: const <String>[],
                  builder: (ctx, snapshot) {
                    if (snapshot.connectionState == ConnectionState.done) {
                      if (snapshot.hasError) {
                        return Center(child: Text(snapshot.error.toString()));
                      } else {
                        if (_uploadRunning) {
                          return const Center(
                              child: CircularProgressIndicator());
                        }

                        var data = snapshot.data as List<FileData>;
                        if (data.isEmpty) {
                          return Card(
                              color: Colors.grey.shade300,
                              child: const Padding(
                                  padding: EdgeInsets.all(10.0),
                                  child: Center(
                                      child: Text(
                                          "No data, please upload some files!"))));
                        } else {
                          return Column(
                              children: data
                                  .map((item) => FileWidget(
                                        content: item.content,
                                        fileName: item.name,
                                        uploadDate: item.uploadDate,
                                        deleteFunction: () async {
                                          await Storage.deleteItem(
                                              item.reference);

                                          setState(() {});
                                        },
                                      ))
                                  .toList());
                        }
                      }
                    }

                    return const Center(child: CircularProgressIndicator());
                  })
            ])));
  }
}
