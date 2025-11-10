package sum25.se.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sum25.se.entity.RoleUsers;
import sum25.se.entity.Users;
import sum25.se.service.IUsersService;

@Controller
public class UserController {

    private final IUsersService iUsersService;

    public UserController(IUsersService iUsersService) {
        this.iUsersService = iUsersService;
    }

//    @PostMapping("/delete/user/{id}")
//    public String deleteUser(@PathVariable int id,
//                             HttpSession session){
//        Users user = (Users) session.getAttribute("LoggedIn");
//        if (user == null) {
//            return "redirect:/main";
//        } else if (!user.getRoleUser().equals(RoleUsers.ADMIN)) {
//            return "403";
//        }
//        iUsersService.deleteUser(id);
//        return "redirect:/schedule/admin/users";
//    }
}
