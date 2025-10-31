package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.Airport;
import sum25.se.repository.IAirportRepository;

import java.util.List;

@Service
public class AirportServiceImpl implements IAirportService {
    @Autowired
    IAirportRepository iAirportRepository;


    @Override
    public List<Airport> getAllAirports() {
        return iAirportRepository.findAll();
    }

    @Override
    public Airport getAirportById(Integer id) {
        return iAirportRepository.getAirportByAirportId(id);
    }

    @Override
    public Airport createAirport(Airport airport) {
        return iAirportRepository.save(airport);
    }

    @Override
    public Airport updateAirport(Integer id, Airport airport) {
        Airport existing = iAirportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found"));
        existing.setAirportName(airport.getAirportName());
        existing.setCode(airport.getCode());
        existing.setLocation(airport.getLocation());
        return iAirportRepository.save(existing);
    }

    @Override
    public void deleteAirport(Integer id) {
        iAirportRepository.deleteById(id);
    }


}
