package sum25.se.service;

import org.springframework.scheduling.annotation.Scheduled;
import sum25.se.entity.FlightSchedule;

import java.util.List;

public interface IFlightScheduleService {
    List<FlightSchedule> getAllSchedules();

    FlightSchedule getScheduleById(Integer id);

    FlightSchedule createSchedule(FlightSchedule schedule);

    FlightSchedule updateSchedule(Integer id, FlightSchedule schedule);

    void deleteSchedule(Integer id);

    FlightSchedule addFlightSchedulte(FlightSchedule flightSchedule);
}
