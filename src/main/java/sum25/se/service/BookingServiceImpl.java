package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.Booking;
import sum25.se.repository.IBookingRepository;

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
    public Booking getBookingById(Integer id) {
        return iBookingRepository.getBookingByBookingId(id);
    }

    @Override
    public Booking createBooking(Booking booking) {
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
        existing.setFlight(booking.getFlight());
        return iBookingRepository.save(existing);
    }

    @Override
    public void deleteBooking(Integer id) {
        iBookingRepository.deleteById(id);
    }
}
