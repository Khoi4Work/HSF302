package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Flight;

public interface IFlightRepository extends JpaRepository<Flight,Integer> {
    Flight getFlightByFlightId(Integer flightId);
}
