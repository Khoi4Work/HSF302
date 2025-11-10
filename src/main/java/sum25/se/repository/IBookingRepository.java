package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Booking;
import sum25.se.entity.Users;

import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking,Integer> {
    Booking getBookingByBookingId(Integer bookingId);
    List<Booking> findByUsers(Users users);
}
