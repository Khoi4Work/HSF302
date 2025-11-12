package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.Booking;
import sum25.se.entity.Plane;
import sum25.se.entity.PassengerInfo;
import sum25.se.entity.Users;
import sum25.se.repository.IBookingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService {

    @Autowired
    private IBookingRepository iBookingRepository;


    @Override
    public List<Booking> getAllBookings() {
        return iBookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByUser(Users user) {
        return iBookingRepository.findByUsers(user);
    }

    @Override
    public Booking getBookingById(Integer id) {
        return iBookingRepository.getBookingByBookingId(id);
    }

    @Override
    public Booking createBooking(String seatClass,
                                 Integer totalPrice,
                                 Users user,
                                 Plane plane) {
        // Tạo Booking với user đang đăng nhập
        Booking booking = new Booking();
        booking.setPlane(plane);
        booking.setSeatClass(seatClass);
        booking.setTotalPrice(totalPrice);
        booking.setUsers(user); // Gán user đang đăng nhập
        // Tự động set ngày đặt vé nếu chưa có
        if (booking.getBookingDate() == null || booking.getBookingDate().isEmpty()) {
            booking.setBookingDate(LocalDateTime.now().toString());
        }
        return iBookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Integer id, Booking booking) {
        Booking existing = iBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        existing.setBookingDate(booking.getBookingDate());
        existing.setStatus(booking.getStatus());
        existing.setTotalPrice(booking.getTotalPrice());
        existing.setSeatClass(booking.getSeatClass());
        existing.setUsers(booking.getUsers());
        existing.setPlane(booking.getPlane());
        return iBookingRepository.save(existing);
    }

    @Override
    public void deleteBooking(Integer id) {
        // Xóa booking, PassengerInfo sẽ tự động bị xóa do cascade
        iBookingRepository.deleteById(id);
    }

    @Override
    public Booking createBooking(Users user, Plane plane, List<PassengerInfo> passengers, String seatClass, int totalPrice) {
        Booking booking = new Booking();
        booking.setUsers(user);
//        booking.setFlight(plane);
        booking.setSeatClass(seatClass);
        booking.setBookingDate(LocalDateTime.now().toString());
        booking.setTotalPrice(totalPrice);
        booking.setPassengerInfos(passengers);

        return iBookingRepository.save(booking);
    }

    @Override
    public Booking updatePassengerInfoAndGetBooking(Integer bookingId, String fullName,
                                                    String gender, String passportNumber,
                                                    String dateOfBirthStr,
                                                    IPassengerInfoService passengerInfoService) {

        Booking booking = iBookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        PassengerInfo passengerInfo = null;
        if (booking.getPassengerInfos() != null && !booking.getPassengerInfos().isEmpty()) {
            passengerInfo = booking.getPassengerInfos().get(0);
        } else {
            passengerInfo = new PassengerInfo();
            passengerInfo.setBooking(booking);
        }
        passengerInfo.setFullName(fullName);
        passengerInfo.setGender(gender);
        passengerInfo.setPassportNumber(passportNumber);

        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            try {
                passengerInfo.setDateOfBirth(LocalDate.parse(dateOfBirthStr));
            } catch (Exception e) {
                System.out.println(" Không parse được ngày sinh: " + e.getMessage());
            }
        }
        if (passengerInfo.getPassengerId() != null) {
            passengerInfoService.updatePassenger(passengerInfo.getPassengerId(), passengerInfo);
            System.out.println(" Đã cập nhật PassengerInfo ID: " + passengerInfo.getPassengerId());
        } else {
            PassengerInfo saved = passengerInfoService.createPassenger(passengerInfo);
            System.out.println(" Đã tạo mới PassengerInfo ID: " + saved.getPassengerId());
        }
        return iBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found after update"));

    }
}

