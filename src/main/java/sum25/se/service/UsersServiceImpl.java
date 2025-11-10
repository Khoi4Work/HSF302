package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.Users;
import sum25.se.repository.IUsersRepository;

import java.util.List;

@Service
public class UsersServiceImpl implements IUsersService {
    @Autowired
    private IUsersRepository iUsersRepository;

    @Override
    public List<Users> getAllUsers() {
        return iUsersRepository.findAll();
    }

    @Override
    public Users getUserById(Integer id) {
        return iUsersRepository.getUsersByUserId(id);
    }

    @Override
    public Users createUser(Users user) {
        return iUsersRepository.save(user);
    }

    @Override
    public Users updateUser(Integer id, Users updatedUser) {
        return iUsersRepository.findById(id)
                .map(existing -> {
                    existing.setFullName(updatedUser.getFullName());
                    existing.setEmail(updatedUser.getEmail());
                    existing.setPassword(updatedUser.getPassword());
                    existing.setPhone(updatedUser.getPhone());
                    existing.setPassportNumber(updatedUser.getPassportNumber());
                    existing.setDateOfBirth(updatedUser.getDateOfBirth());
                    existing.setRoleUser(updatedUser.getRoleUser());
                    return iUsersRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Integer id) {
        iUsersRepository.deleteById(id);
    }

    @Override
    public Users login(String email, String password) {
        return iUsersRepository.getUsersByEmailAndPassword(email, password);
    }
}
