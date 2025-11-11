package sum25.se.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;
    @NotBlank(message = "Full name không được để trống")
    @Column(columnDefinition = "NVARCHAR(100)")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Full name chỉ được chứa chữ và khoảng trắng")
    String fullName;
    @NotBlank(message = "Please enter your email")
    @Email(message = "Email không hợp lệ")
    String email;
    @NotBlank(message = "Please enter your password")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    String password;
    @NotBlank(message = "Please enter your phone number")
    @Pattern(regexp = "^[0-9]{9,10}$", message = "Phone number must be 9-10 digits")
    String phone;
    String passportNumber;
    LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    StatusUsers status = StatusUsers.ACTIVE;
    @Enumerated(EnumType.STRING)
    RoleUsers roleUser = RoleUsers.USER;
    @OneToMany(mappedBy = "users")
    List<Booking> bookings;

}
