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
public class FlightSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;
    private int seatNumber;
    private String seatClass;
    private double price;
    private String status;

    @OneToMany(mappedBy = "flightSchedule")
    List<FlightSchedule_Plane> flightSchedulePlane;



}
