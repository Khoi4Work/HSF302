package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Users;

public interface IUsersRepository extends JpaRepository<Users,Integer> {
    Users getUsersByUserId(Integer userId);
}
