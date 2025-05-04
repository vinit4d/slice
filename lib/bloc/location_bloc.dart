import 'package:bloc/bloc.dart';

import '../utlis/native_services.dart';

part 'location_event.dart';
part 'location_state.dart';

class LocationBloc extends Bloc<LocationEvent, LocationState> {
  LocationBloc() : super(LocationInitial()) {
    on<LoadLocationInitial>(_onLoadInitialLocation);
  }

  void _onLoadInitialLocation(LoadLocationInitial event, Emitter<LocationState> emit) async {
    final location = await NativeServices.getLocation();
    emit(LocationLoadedState(
      double.parse(location?['latitude'].toString()??"0.0"),
      double.parse(location?['longitude'].toString()??"0.0"),
    ));
  }


}