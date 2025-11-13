package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    StatusBooking status = StatusBooking.PENDING;
    int totalPrice;
    String seatClass;
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    Users users;
    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<PassengerInfo> passengerInfos;
    @OneToMany(mappedBy = "booking")
    List<Payment> payments;
    @OneToOne
    @JoinColumn(name = "schedule_plane")
    FlightSchedule_Plane schedule_plane;



}
