package sum25.se.service;

import sum25.se.entity.Flight;

import java.util.List;

public interface IFlightService {
    List<Flight> getAllFlights();
    Flight getFlightById(Integer id);
    Flight createFlight(Flight flight);
    Flight updateFlight(Integer id, Flight flight);
    void deleteFlight(Integer id);
}
