package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.FlightSchedule;
import sum25.se.repository.IFlightScheduleRepository;

import java.util.List;

@Service
public class FlightScheduleServiceImpl implements IFlightScheduleService {

    @Autowired
    private IFlightScheduleRepository iFlightScheduleRepository;

    @Override
    public List<FlightSchedule> getAllSchedules() {
        return iFlightScheduleRepository.findAll();
    }

    @Override
    public FlightSchedule getScheduleById(Integer id) {
        return iFlightScheduleRepository.getFlightScheduleByScheduleId(id);
    }

    @Override
    public FlightSchedule createSchedule(FlightSchedule schedule) {
        return iFlightScheduleRepository.save(schedule);
    }

    @Override
    public FlightSchedule updateSchedule(Integer id, FlightSchedule schedule) {
        FlightSchedule existing = getScheduleById(id);

        existing.setSeatNumber(schedule.getSeatNumber());
        existing.setSeatClass(schedule.getSeatClass());
        existing.setPrice(schedule.getPrice());
        existing.setStatus(schedule.getStatus());
        existing.setFlight(schedule.getFlight());

        return iFlightScheduleRepository.save(existing);
    }

    @Override
    public void deleteSchedule(Integer id) {
        iFlightScheduleRepository.deleteById(id);
    }
}
