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
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flightId;
    private String planeModel;
    private LocalDateTime departureTime;
    private int duration;
    // save duration in minutes
    String status;
    @OneToMany(mappedBy = "flight")
    private List<Booking> bookings;
    @ManyToOne
    @JoinColumn(name = "airportId")
    private Airport airport;
    @OneToMany(mappedBy = "flight")
    private List<FlightSchedule> flightSchedules;

}
