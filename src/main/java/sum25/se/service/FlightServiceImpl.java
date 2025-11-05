package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.Plane;
import sum25.se.entity.Plane;
import sum25.se.repository.IFlightRepository;

import java.util.List;

@Service
public class FlightServiceImpl implements IFlightService {

    @Autowired
    private IFlightRepository iFlightRepository;


    @Override
    public List<Plane> getAllFlights() {
        return iFlightRepository.findAll();
    }

    @Override
    public Plane getFlightById(Integer id) {
        return iFlightRepository.getFlightByFlightId(id);
    }

    @Override
    public Plane createFlight(Plane plane) {
        return iFlightRepository.save(plane);
    }

    @Override
    public Plane updateFlight(Integer id, Plane plane) {
        Plane existing = getFlightById(id);

        existing.setPlaneModel(plane.getPlaneModel());
        existing.setDepartureTime(plane.getDepartureTime());
        existing.setDuration(plane.getDuration());
        existing.setStatus(plane.getStatus());
        existing.setAirport(plane.getAirport());

        return iFlightRepository.save(existing);
    }

    @Override
    public void deleteFlight(Integer id) {
        iFlightRepository.deleteById(id);
    }

    @Override
    public Plane addFlight(Plane plane) {
        return iFlightRepository.save(plane);
    }
}
