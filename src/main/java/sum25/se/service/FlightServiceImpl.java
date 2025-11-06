package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.FlightSchedule_Plane;
import sum25.se.entity.Plane;
import sum25.se.entity.Plane;
import sum25.se.repository.IFlightRepository;
import sum25.se.repository.IFlightSchedulePlaneRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightServiceImpl implements IFlightService {

    @Autowired
    private IFlightRepository iFlightRepository;
    @Autowired
    private IFlightSchedulePlaneRepository iFlightSchedulePlaneRepository;


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

    @Override
    public List<FlightSchedule_Plane> searchFlights(String departure, String destination, LocalDate date, String seatClass) {

        // Nếu không chọn hạng ghế → chỉ tìm theo điểm đi, điểm đến, ngày
        if (seatClass == null || seatClass.isEmpty()) {
            return searchWithoutSeatClass(departure, destination, date);
        }

        // Nếu có chọn hạng ghế → tìm theo hạng ghế cụ thể
        return searchWithSeatClass(departure, destination, date, seatClass);
    }

    private List<FlightSchedule_Plane> searchWithoutSeatClass(String departure, String destination, LocalDate date) {
        return iFlightSchedulePlaneRepository
                .findByTakeOffAndLandAndTakeOffTimeBetween(
                        departure,
                        destination,
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay()
                );
    }

    private List<FlightSchedule_Plane> searchWithSeatClass(String departure, String destination, LocalDate date, String seatClass) {
        return iFlightSchedulePlaneRepository
                .findByTakeOffAndLandAndTakeOffTimeBetweenAndFlightSchedule_SeatClass(
                        departure,
                        destination,
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay(),
                        seatClass
                );
    }


}
