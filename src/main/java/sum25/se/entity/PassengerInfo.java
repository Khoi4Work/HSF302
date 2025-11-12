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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    Booking booking;
}
