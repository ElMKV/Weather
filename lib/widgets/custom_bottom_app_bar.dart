import 'package:flutter/material.dart';

class CustomBottomAppBar extends StatelessWidget {
  final bool showMediumLink;
  final bool showDonationLink;
  final bool showGitHubLink;
  final String? customMediumLink;

  const CustomBottomAppBar(
      {Key? key,
      this.showMediumLink = true,
      this.showDonationLink = true,
      this.showGitHubLink = true,
      this.customMediumLink})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BottomAppBar(
        color: Colors.grey.shade300,
        child: Text(' ojdfvojfdv'));
  }
}
