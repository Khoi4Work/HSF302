package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.FlightSchedule_Plane;

import java.time.LocalDateTime;
import java.util.List;

public interface IFlightSchedulePlaneRepository extends JpaRepository<FlightSchedule_Plane, Integer> {

    List<FlightSchedule_Plane> findByTakeOffAndLandAndTakeOffTimeBetween(String takeOff, String land, LocalDateTime takeOffTimeAfter, LocalDateTime takeOffTimeBefore);

    List<FlightSchedule_Plane> findByTakeOffAndLandAndTakeOffTimeBetweenAndFlightSchedule_SeatClass(String takeOff, String land, LocalDateTime takeOffTimeAfter, LocalDateTime takeOffTimeBefore, String flightScheduleSeatClass);

    FlightSchedule_Plane findByFlightPlaneId(int flightPlaneId);
}
