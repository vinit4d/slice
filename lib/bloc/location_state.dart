
part of 'location_bloc.dart';
@override
abstract class LocationState {}

class LocationInitial extends LocationState {}

class LocationLoadedState extends LocationState {
  final double latitude;
  final double longitude;

  LocationLoadedState(this.latitude, this.longitude);
}

class LocationErrorState extends LocationState {
  final String msg;
  LocationErrorState(this.msg);
}
