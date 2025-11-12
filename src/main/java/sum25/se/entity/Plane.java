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
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planeId")
    private Integer flightId;
    private String planeModel;
    private LocalDateTime departureTime;
    private int duration;
    // save duration in minutes
    String status;
//    @OneToMany(mappedBy = "plane")
//    private List<Booking> bookings;
    @ManyToOne
    @JoinColumn(name = "airportId")
    private Airport airport;

    @OneToMany(mappedBy = "plane")
    List<FlightSchedule_Plane> flightSchedulePlane;

}
