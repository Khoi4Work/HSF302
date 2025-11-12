package sum25.se.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sum25.se.entity.FlightSchedule_Plane;
import sum25.se.entity.RoleUsers;
import sum25.se.entity.StatusUsers;
import sum25.se.entity.Users;
import sum25.se.service.IAirportService;
import sum25.se.service.IFlightSchedulePlaneService;
import sum25.se.service.IUsersService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private IUsersService iUsersService;
    @Autowired
    private IFlightSchedulePlaneService iFlightSchedulePlaneService;
    @Autowired
    private IAirportService iAirportService;

    @GetMapping("/")
    public String mainPage(Model model) {
        List<FlightSchedule_Plane> flightList = iFlightSchedulePlaneService.findAll();
        model.addAttribute("airports", iAirportService.getAllAirports());
        model.addAttribute("flights", flightList);
        return "main";
    }


    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView showRegister() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new Users());
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register-success")
    public String showRegisterSuccess(@Valid @ModelAttribute("user") Users user,
                                      BindingResult bindingResult,
                                      Model model) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);

            // Validate tuổi ≥18
            if (user.getDateOfBirth() != null &&
                    user.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
                // Thêm lỗi vào BindingResult
                bindingResult.rejectValue(
                        "dateOfBirth", // tên field trong DTO/entity
                        "error.user",  // mã lỗi (có thể tuỳ ý)
                        "Bạn phải trên 18 tuổi" // thông báo lỗi
                );
                return "register";
            }

            return "register";
        }

        if (user.getDateOfBirth() != null &&
                user.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
            // Thêm lỗi vào BindingResult
            bindingResult.rejectValue(
                    "dateOfBirth", // tên field trong DTO/entity
                    "error.user",  // mã lỗi (có thể tuỳ ý)
                    "Bạn phải trên 18 tuổi" // thông báo lỗi
            );
            return "register";
        }

        iUsersService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/main")
    public String showMain(Model model) {
        System.out.println("reset find");
        List<FlightSchedule_Plane> flightList = iFlightSchedulePlaneService.findAll();
        model.addAttribute("airports", iAirportService.getAllAirports());
        model.addAttribute("flights", flightList);
        return "main";
    }

    @GetMapping("/error")
    public String showError() {
        return "error";
    }

    @GetMapping("/mainPage")
    public String showMainPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/main";
        }

        Boolean loginSuccess = (Boolean) session.getAttribute("loginSuccess");
        if (loginSuccess != null && loginSuccess) {
            model.addAttribute("loginSuccess", true);
            session.removeAttribute("loginSuccess");
        }

        model.addAttribute("user", user);
        model.addAttribute("airports", iAirportService.getAllAirports());
        List<FlightSchedule_Plane> flightList = iFlightSchedulePlaneService.findAll();
        model.addAttribute("flights", flightList);
        return "mainPage";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "redirect:/schedule/admin";
    }

    @PostMapping("/processLogin")
    public String processLogin(HttpSession session,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        Users user = iUsersService.login(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid Email or number password");
            return "login";
        }

        session.setAttribute("LoggedIn", user);
        session.setAttribute("loginSuccess", true);

        if (user.getRoleUser() == RoleUsers.ADMIN) {
            return "redirect:/schedule/admin";
        } else {
            return "redirect:/mainPage";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
