package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flightSchedulePlane")
public class FlightSchedule_Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int flightPlaneId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planeId")
    private Plane plane;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheduleId")
    private FlightSchedule flightSchedule;
    private String takeOff;
    private String land;
    private LocalDateTime takeOffTime;
    private LocalDateTime landTime;

    public LocalDate getTakeOffDate() {
        return this.takeOffTime != null ? this.takeOffTime.toLocalDate() : null;
    }

    public LocalTime getTakeOffHour() {
        return this.takeOffTime != null ? this.takeOffTime.toLocalTime() : null;
    }

    public LocalDate getLandDate() {
        return this.landTime != null ? this.landTime.toLocalDate() : null;
    }

    public LocalTime getLandHour() {
        return this.landTime != null ? this.landTime.toLocalTime() : null;
    }

}
