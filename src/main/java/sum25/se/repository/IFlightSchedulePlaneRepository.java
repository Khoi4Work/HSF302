package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.FlightSchedule_Plane;

public interface IFlightSchedulePlaneRepository extends JpaRepository<FlightSchedule_Plane, Integer> {
}
