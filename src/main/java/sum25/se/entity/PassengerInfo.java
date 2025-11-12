package sum25.se.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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
<<<<<<< HEAD
    LocalDate dateOfBirth;
=======
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    Date dateOfBirth;
>>>>>>> 15b1797884603b30a8722acaa68288455f888c1b
    @ManyToOne
    @JoinColumn(name = "bookingId")
    Booking booking;
}
