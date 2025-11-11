package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer passengerId;
    @Column(columnDefinition = "NVARCHAR(10)")
    String gender;
    @Column(columnDefinition = "NVARCHAR(100)")
    String fullName;
    String passportNumber;
    Date dateOfBirth;
    @ManyToOne
    @JoinColumn(name= "bookingId")
    Booking booking;
}
