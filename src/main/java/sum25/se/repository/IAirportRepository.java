package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Airport;

public interface IAirportRepository extends JpaRepository<Airport,Integer> {
    Airport getAirportByAirportId(Integer airportId);

    Airport findByAirportName(String airportName);

    Airport findByCode(String code);
}
