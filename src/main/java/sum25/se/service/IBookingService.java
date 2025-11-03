package sum25.se.service;

import sum25.se.entity.Booking;
import sum25.se.entity.Flight;
import sum25.se.entity.PassengerInfo;
import sum25.se.entity.Users;

import java.util.List;

public interface IBookingService {
    List<Booking> getAllBookings();
    Booking getBookingById(Integer id);
    Booking createBooking(Booking booking);
    Booking updateBooking(Integer id, Booking booking);
    void deleteBooking(Integer id);
    Booking createBooking(Users user, Flight flight, List<PassengerInfo> passengers, String seatClass, int totalPrice);
}
