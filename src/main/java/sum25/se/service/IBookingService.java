package sum25.se.service;

import sum25.se.entity.*;

import java.util.List;

public interface IBookingService {
    List<Booking> getAllBookings();

    List<Booking> getBookingsByUser(Users user);

    Booking getBookingById(Integer id);

    Booking createBooking(String seatClass,
                          Integer totalPrice,
                          Users user,
                          FlightSchedule_Plane schedule_plane);

    Booking updateBooking(Integer id, Booking booking);

    void deleteBooking(Integer id);

    Booking createBooking(Users user, Plane plane, List<PassengerInfo> passengers, String seatClass, int totalPrice);

    Booking updatePassengerInfoAndGetBooking(Integer bookingId,
                                             String fullName,
                                             String gender,
                                             String passportNumber,
                                             String dateOfBirthStr,
                                             IPassengerInfoService passengerInfoService);
}
