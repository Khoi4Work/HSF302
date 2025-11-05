package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Plane;
import sum25.se.entity.Plane;

public interface IFlightRepository extends JpaRepository<Plane,Integer> {
    Plane getFlightByFlightId(Integer flightId);
}
