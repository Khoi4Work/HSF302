package sum25.se.service;

import sum25.se.entity.FlightSchedule_Plane;
import sum25.se.entity.Plane;
import sum25.se.entity.Plane;

import java.time.LocalDate;
import java.util.List;

public interface IFlightService {
    List<Plane> getAllFlights();
    Plane getFlightById(Integer id);
    Plane createFlight(Plane plane);
    Plane updateFlight(Integer id, Plane plane);
    void deleteFlight(Integer id);
    Plane addFlight(Plane plane);

    List<FlightSchedule_Plane> searchFlights(String departure, String destination, LocalDate date, String seatClass);

    FlightSchedule_Plane getFlightPlaneByFlightId(Integer flightId);
}
