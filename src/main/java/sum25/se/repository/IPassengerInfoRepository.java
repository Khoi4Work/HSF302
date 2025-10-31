package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.PassengerInfo;

public interface IPassengerInfoRepository extends JpaRepository<PassengerInfo,Integer> {
    PassengerInfo getPassengerInfoByPassengerId(Integer passengerId);
}
