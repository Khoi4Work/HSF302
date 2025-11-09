package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.FlightSchedule_Plane;
import sum25.se.repository.IFlightSchedulePlaneRepository;

import java.util.List;

@Service
public class FlightSchedulePlaneService implements IFlightSchedulePlaneService {
    @Autowired
    IFlightSchedulePlaneRepository flightSchedulePlaneRepository;

    @Override
    public FlightSchedule_Plane add(FlightSchedule_Plane flightSchedulePlane) {
        return flightSchedulePlaneRepository.save(flightSchedulePlane);
    }

    @Override
    public List<FlightSchedule_Plane> findAll() {
        return flightSchedulePlaneRepository.findAll();
    }

    @Override
    public void deleteSchedule(int id) {
        flightSchedulePlaneRepository.deleteById(id);
    }

    @Override
    public FlightSchedule_Plane findById(int id) {
        return flightSchedulePlaneRepository.findByFlightPlaneId(id);
    }
}
