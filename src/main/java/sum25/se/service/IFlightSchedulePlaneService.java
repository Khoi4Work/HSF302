package sum25.se.service;

import sum25.se.entity.FlightSchedule_Plane;

import java.util.List;

public interface IFlightSchedulePlaneService {
    public FlightSchedule_Plane add(FlightSchedule_Plane flightSchedulePlane);

    public List<FlightSchedule_Plane> findAll();
}
