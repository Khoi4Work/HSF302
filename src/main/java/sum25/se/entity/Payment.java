package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;
    LocalDateTime paymentDate;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;
    @ManyToOne
    @JoinColumn(name= "bookingId")
    Booking booking;

}
