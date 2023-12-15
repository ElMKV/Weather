import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:hexcolor/hexcolor.dart';
import 'package:weather/firebase/auth.dart';
import 'package:weather/futures/core_widgets/custom_button.dart';
import 'package:weather/futures/main/homePage.dart';

class AuthPage extends StatefulWidget {
  const AuthPage({Key? key}) : super(key: key);

  @override
  State<AuthPage> createState() => _State();
}

class _State extends State<AuthPage> {
  String _pwd = "";
  String _mail = "";
  User? _user;
  bool _viewPassowrd = true;

  @override
  void initState() {
    super.initState();

    FirebaseAuth.instance.authStateChanges().listen((user) {
      setState(() {
        _user = user;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(32.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(
                height: 40,
              ),
              Text(
                'Вход',
                style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold),
              ),
              SizedBox(
                height: 20,
              ),
              Text(
                'Введите данные для входа',
                style: TextStyle(fontSize: 15, color: HexColor('#8799A5')),
              ),
              Column(children: [
                Container(height: 60),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 10),
                  child: TextField(

                    onChanged: (value) {
                      setState(() {
                        _mail = value;
                      });
                    },
                    decoration: InputDecoration(
                      label: Text("Email"),

                      prefixIconColor: MaterialStateColor.resolveWith(
                          (Set<MaterialState> states) {
                        if (states.contains(MaterialState.focused)) {
                          return Colors.blue;
                        }
                        if (states.contains(MaterialState.error)) {
                          return Colors.red;
                        }
                        return Colors.grey;
                      }),
                    ),
                  ),
                ),
                Container(height: 10),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 10),
                  child: TextField(
                      obscureText: _viewPassowrd,
                      enableSuggestions: false,
                      autocorrect: false,
                      onChanged: (value) {
                        setState(() {
                          _pwd = value;
                        });
                      },
                      decoration: InputDecoration(
                        label: Text("Пароль"),
                        suffixIcon: IconButton(
                            onPressed: ()  {
                              setState(() {
                                _viewPassowrd = !_viewPassowrd;
                              });
                            },
                            icon: Icon(Icons.visibility_outlined, color: _viewPassowrd ? Colors.blue : Colors.red,)),
                      )),
                ),
                Container(height: 60),
                Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      CustomButton(
                          width: MediaQuery.of(context).size.width/1.3,
                          height: 48,
                          text: 'Вход',
                          onTap: () async {
                            var res = await Auth.mailSignIn(_mail, _pwd);
                            ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                                backgroundColor:
                                    res == null ? Colors.green : Colors.red,
                                content: Text(res ??
                                    "Вы вошли под аккаунтом ${_user?.email}")));
                            Navigator.of(context).push(MaterialPageRoute(
                                builder: (context) => const HomePage()));
                          }),
                    ]),
              ]),
            ],
          ),
        ),
      ),
    );
  }
}
