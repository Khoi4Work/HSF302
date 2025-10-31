package sum25.se.service;

import sum25.se.entity.Booking;

import java.util.List;

public interface IBookingService {
    List<Booking> getAllBookings();
    Booking getBookingById(Integer id);
    Booking createBooking(Booking booking);
    Booking updateBooking(Integer id, Booking booking);
    void deleteBooking(Integer id);
}
