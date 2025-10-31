package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.Flight;
import sum25.se.repository.IFlightRepository;

import java.util.List;

@Service
public class FlightServiceImpl implements IFlightService {

    @Autowired
    private IFlightRepository iFlightRepository;


    @Override
    public List<Flight> getAllFlights() {
        return iFlightRepository.findAll();
    }

    @Override
    public Flight getFlightById(Integer id) {
        return iFlightRepository.getFlightByFlightId(id);
    }

    @Override
    public Flight createFlight(Flight flight) {
        return iFlightRepository.save(flight);
    }

    @Override
    public Flight updateFlight(Integer id, Flight flight) {
        Flight existing = getFlightById(id);

        existing.setPlaneModel(flight.getPlaneModel());
        existing.setDepartureTime(flight.getDepartureTime());
        existing.setDuration(flight.getDuration());
        existing.setStatus(flight.getStatus());
        existing.setAirport(flight.getAirport());

        return iFlightRepository.save(existing);
    }

    @Override
    public void deleteFlight(Integer id) {
        iFlightRepository.deleteById(id);
    }
}
