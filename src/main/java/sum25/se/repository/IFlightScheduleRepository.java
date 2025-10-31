package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.FlightSchedule;

public interface IFlightScheduleRepository extends JpaRepository<FlightSchedule,Integer> {
    FlightSchedule getFlightScheduleByScheduleId(Integer scheduleId);
}
