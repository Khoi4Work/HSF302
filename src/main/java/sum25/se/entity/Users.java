package sum25.se.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;

    String fullName;
    String email;
    String password;
    String phone;
    String passportNumber;
    Date dateOfBirth;
    @ManyToOne
    @JoinColumn(name = "roleId")
    UserRole role;
    @OneToMany(mappedBy = "users")
    List<Booking> bookings;

}
