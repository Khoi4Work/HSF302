package sum25.se.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sum25.se.entity.Users;
import sum25.se.service.IUsersService;

@Controller
public class LoginController {
    @Autowired
    private IUsersService iUsersService;

    @GetMapping()
    public String mainPage(){
        return "main";
    }

    @GetMapping("/login")
    public String showLogin(){
        return "login";
    }
    @GetMapping("/register")
    public ModelAndView showRegister(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new Users());
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register-success")
    public String showRegisterSuccess( Users user){
        System.out.println(user);
        iUsersService.createUser(user);
        return "redirect:/login";
    }


    @PostMapping("/process")
    public String processLogin(HttpSession session, @RequestParam String email, @RequestParam String password) {
        Users user = iUsersService.login(email, password);
        if (user == null) {
            return "redirect:/error";
        }
        System.out.println(email+"-"+password);
        session.setAttribute("LoggedIn", user);
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/error")
    public String showError() {
        return "error";
    }
}
