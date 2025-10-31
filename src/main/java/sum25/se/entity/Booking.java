package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer bookingId;
    String bookingDate;
    String status;
    int totalPrice;
    String seatClass;
    @ManyToOne
    @JoinColumn(name = "userId")
    Users users;
    @OneToMany(mappedBy = "booking")
    List<PassengerInfo> passengerInfos;
    @OneToMany(mappedBy = "booking")
    List<Payment> payments;
    @ManyToOne
    @JoinColumn(name = "flightId")
    Flight flight;

}
