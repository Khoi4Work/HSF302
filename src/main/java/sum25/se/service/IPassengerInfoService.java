package sum25.se.service;

import sum25.se.entity.PassengerInfo;

import java.util.List;

public interface IPassengerInfoService {
    List<PassengerInfo> getAllPassengers();
    PassengerInfo getPassengerById(Integer id);
    PassengerInfo createPassenger(PassengerInfo passenger);
    PassengerInfo updatePassenger(Integer id, PassengerInfo passenger);
    void deletePassenger(Integer id);
}
