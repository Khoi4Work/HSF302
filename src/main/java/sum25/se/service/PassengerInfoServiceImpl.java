package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.PassengerInfo;
import sum25.se.repository.IPassengerInfoRepository;

import java.util.List;

@Service
public class PassengerInfoServiceImpl implements IPassengerInfoService {

    @Autowired
    private IPassengerInfoRepository iPassengerInfoRepository;

    @Override
    public List<PassengerInfo> getAllPassengers() {
        return iPassengerInfoRepository.findAll();
    }

    @Override
    public PassengerInfo getPassengerById(Integer id) {
        return iPassengerInfoRepository.getPassengerInfoByPassengerId(id);
    }

    @Override
    public PassengerInfo createPassenger(PassengerInfo passenger) {
        return iPassengerInfoRepository.save(passenger);
    }

    @Override
    public PassengerInfo updatePassenger(Integer id, PassengerInfo passenger) {
        PassengerInfo existing = getPassengerById(id);
        if (existing == null) {
            throw new RuntimeException("PassengerInfo not found with id: " + id);
        }

        existing.setGender(passenger.getGender());
        existing.setFullName(passenger.getFullName());
        existing.setPassportNumber(passenger.getPassportNumber());
        existing.setDateOfBirth(passenger.getDateOfBirth());
        // Không cập nhật booking để tránh lỗi circular reference
        // existing.setBooking(passenger.getBooking());

        return iPassengerInfoRepository.save(existing);
    }

    @Override
    public void deletePassenger(Integer id) {
        iPassengerInfoRepository.deleteById(id);
    }
}
