package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    String fullName;
    String email;
    String password;
    String phone;
    String passportNumber;
    LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    RoleUsers roleUses = RoleUsers.USER;
    @OneToMany(mappedBy = "users")
    List<Booking> bookings;

}
