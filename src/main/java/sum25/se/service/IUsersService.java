package sum25.se.service;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Users;

import java.util.List;

public interface IUsersService {
    List<Users> getAllUsers();
    Users getUserById(Integer id);
    Users createUser(Users user);
    Users updateUser(Integer id, Users updatedUser);
    void deleteUser(Integer id);

    Users login(String email, String password);
}
