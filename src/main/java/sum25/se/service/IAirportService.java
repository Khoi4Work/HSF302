package sum25.se.service;

import sum25.se.entity.Airport;

import java.util.List;

public interface IAirportService {
    List<Airport> getAllAirports();

    Airport getAirportById(Integer id);

    Airport createAirport(Airport airport);

    Airport updateAirport(Integer id, Airport airport);

    void deleteAirport(Integer id);

    Airport getAirpotByAirportName(String airportName);

    Airport addAirport(Airport airport);

    Airport getAiportByCode(String code);
}
