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
    @Enumerated(EnumType.STRING)
    StatusBooking status =StatusBooking.PENDING;
    int totalPrice;
    String seatClass;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    Users users;
    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<PassengerInfo> passengerInfos;
    @OneToMany(mappedBy = "booking")
    List<Payment> payments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flightId")
    Plane plane;

}
