package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Booking;

public interface IBookingRepository extends JpaRepository<Booking,Integer> {
    Booking getBookingByBookingId(Integer bookingId);
}
