package sum25.se.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sum25.se.entity.RoleUsers;
import sum25.se.entity.Users;
import sum25.se.service.IUsersService;

import java.time.LocalDate;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    IUsersService iUsersService;

    @Override
    public void run(String... args) throws Exception {


        // Nếu database chưa có user nào thì tạo mới
        if (iUsersService.getAllUsers().isEmpty()) {
            Users admin = new Users();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@skyticket.com");
            admin.setPassword("admin123"); // 👉 nên mã hóa password bằng BCrypt trong thực tế
            admin.setPhone("0123456789");
            admin.setPassportNumber("VN0000001");
            admin.setDateOfBirth(LocalDate.of(90, 1, 1)); // 1990-01-01
            admin.setRoleUses(RoleUsers.ADMIN);

            Users demoUser = new Users();
            demoUser.setFullName("Demo User");
            demoUser.setEmail("user@skyticket.com");
            demoUser.setPassword("user123");
            demoUser.setPhone("0987654321");
            demoUser.setPassportNumber("VN0000002");
            demoUser.setDateOfBirth(LocalDate.of(95, 5, 15)); // 1995-06-15
            demoUser.setRoleUses(RoleUsers.USER);

            iUsersService.createUser(admin);
            iUsersService.createUser(demoUser);

            System.out.println("✅ Default users initialized successfully!");
        } else {
            System.out.println("ℹ️ Users already exist — skipping initialization.");
        }
    }
}
