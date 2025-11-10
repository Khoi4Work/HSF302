package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.FlightSchedule_Plane;
import sum25.se.repository.IFlightSchedulePlaneRepository;

import java.util.List;

@Service
public class FlightSchedulePlaneService implements IFlightSchedulePlaneService {
    @Autowired
    IFlightSchedulePlaneRepository iFlightSchedulePlaneRepository;

    @Override
    public FlightSchedule_Plane add(FlightSchedule_Plane flightSchedulePlane) {
        return iFlightSchedulePlaneRepository.save(flightSchedulePlane);
    }

    @Override
    public List<FlightSchedule_Plane> findAll() {
        return iFlightSchedulePlaneRepository.findAll();
    }

    @Override
    public void deleteSchedule(int id) {
        iFlightSchedulePlaneRepository.deleteById(id);
    }

    @Override
    public FlightSchedule_Plane findById(int id) {
        return iFlightSchedulePlaneRepository.findByFlightPlaneId(id);
    }
}
