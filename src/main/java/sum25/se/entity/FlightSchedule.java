package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;
    private int seatNumber;
    private String seatClass;
    private double price;
    private String status;
    @ManyToOne
    @JoinColumn(name = "flightId")
    private Flight flight;

}
