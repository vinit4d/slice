import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:slice/utlis/native_services.dart';
import 'bloc/location_bloc.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

@override
class _HomeState extends State<Home> {
  @override
  void initState() {
    super.initState();

    NativeServices.startLocationService();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => LocationBloc(),
      child: Scaffold(
        appBar: AppBar(title: const Text("My Location")),
        body: Center(
          child: BlocBuilder<LocationBloc, LocationState>(
            builder: (context, state) {
              if (state is LocationInitial) {
                Future.delayed(const Duration(seconds: 2), () {
                  context.read<LocationBloc>().add(LoadLocationInitial());
                });
              }
              if (state is LocationLoadedState) {
                return Text("latitude: ${state.latitude},longitude: ${state.longitude}");
              } else {
                return const Center(child: SizedBox(
                    height: 30,width: 30,
                    child: CircularProgressIndicator()));
              }
            },
          ),
        ),
      ),
    );
  }
}
