package sum25.se.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sum25.se.entity.*;
import sum25.se.service.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/schedule")
public class AdminController {

    @Autowired
    private IFlightSchedulePlaneService iFlightSchedulePlaneService;

    @Autowired
    private IFlightService iFlightService;

    @Autowired
    private IFlightScheduleService iFlightScheduleService;

    @Autowired
    private IUsersService iUsersService;
    @Autowired
    private IPaymentService iPaymentService;
    @Autowired
    private IAirportService iAirportService;

    private boolean isAdmin(Users user) {
        return user != null && user.getRoleUser() == RoleUsers.ADMIN;
    }

    @GetMapping("/admin")
    public ModelAndView showAdminPage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();

        modelAndView = authorizeAdmin(modelAndView, user);

        if (!Objects.equals(modelAndView.getViewName(), null)) {
            return modelAndView;
        }

        System.out.println(user.getFullName());
        modelAndView.addObject("admin", user);

        Boolean loginSuccess = (Boolean) session.getAttribute("loginSuccess");
        if (loginSuccess != null && loginSuccess) {
            modelAndView.addObject("loginSuccess", true);
            session.removeAttribute("loginSuccess");
        }

        model.addAttribute("airports", iAirportService.getAllAirports());
        List<FlightSchedule_Plane> schedules = iFlightSchedulePlaneService.findAll();
        modelAndView.addObject("schedules", schedules);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("admin");
        return modelAndView;
    }


    @GetMapping("/deleteSchedule/{id}")
    public String deleteSchedule(HttpSession httpSession, @PathVariable int id, Model model) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");

        if (user == null) {
            return "redirect:/login";
        }

        if (!isAdmin(user)) {
            model.addAttribute("error", "Bạn không có quyền thực hiện hành động này!");
            return "403";
        }

        iFlightSchedulePlaneService.deleteSchedule(id);
        return "redirect:/schedule/admin";
    }

    @GetMapping("/addSchedule")
    public ModelAndView showAddNewSchedule(HttpSession session) {
        Users user = (Users) session.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();

        modelAndView = authorizeAdmin(modelAndView, user);

        if (!Objects.equals(modelAndView.getViewName(), null)) {
            return modelAndView;
        }

        modelAndView.addObject("admin", user);
        List<Plane> planes = iFlightService.getAllFlights();
        List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();

        modelAndView.addObject("planes", planes);
        modelAndView.addObject("flightSchedules", flightSchedules);
        modelAndView.addObject("schedule", new FlightSchedule_Plane());
        modelAndView.setViewName("addSchedule");
        return modelAndView;
    }


    @PostMapping("/addSchedule")
    public String addNewSchedule(
            @Valid @ModelAttribute("schedule") FlightSchedule_Plane schedule,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            @RequestParam("planeId") int planeId,
            @RequestParam("scheduleId") int scheduleId) {

        Users user = (Users) session.getAttribute("LoggedIn");

        if (user == null) {
            return "redirect:/login";
        }

        if (!isAdmin(user)) {
            model.addAttribute("error", "Bạn không có quyền thực hiện hành động này!");
            return "403";
        }


        if (bindingResult.hasErrors()) {
            List<Plane> planes = iFlightService.getAllFlights();
            List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();
            model.addAttribute("planes", planes);
            model.addAttribute("flightSchedules", flightSchedules);
            model.addAttribute("schedule", schedule);
            return "addSchedule";
        }

        if (schedule.getTakeOffTime() != null && schedule.getLandTime() != null) {
            if (!schedule.getTakeOffTime().isBefore(schedule.getLandTime())) {
                List<Plane> planes = iFlightService.getAllFlights();
                List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();
                model.addAttribute("planes", planes);
                model.addAttribute("flightSchedules", flightSchedules);
                model.addAttribute("schedule", schedule);
                model.addAttribute("timeError", "⛔ Thời gian cất cánh phải trước thời gian hạ cánh!");
                return "addSchedule";
            }
        }

        Plane plane = iFlightService.getFlightById(planeId);
        FlightSchedule flightSchedule = iFlightScheduleService.getScheduleById(scheduleId);

        schedule.setPlane(plane);
        schedule.setFlightSchedule(flightSchedule);

        iFlightSchedulePlaneService.add(schedule);
        return "redirect:/schedule/admin";
    }


    @GetMapping("/editSchedule/{id}")
    public ModelAndView showEditSchedule(HttpSession session, @PathVariable int id) {
        Users user = (Users) session.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = authorizeAdmin(modelAndView, user);

        if (!Objects.equals(modelAndView.getViewName(), null)) {
            return modelAndView;
        }

        List<Plane> planes = iFlightService.getAllFlights();
        List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();
        FlightSchedule_Plane schedule = iFlightSchedulePlaneService.findById(id);

        modelAndView.addObject("admin", user);
        modelAndView.addObject("planes", planes);
        modelAndView.addObject("flightSchedules", flightSchedules);
        modelAndView.addObject("schedule", schedule);
        modelAndView.setViewName("editSchedule");
        return modelAndView;
    }


    @PostMapping("/editSchedule")
    public String editSchedule(
            @Valid @ModelAttribute("schedule") FlightSchedule_Plane schedule,
            BindingResult bindingResult,
            HttpSession httpSession,
            Model model,
            @RequestParam("planeId") int planeId,
            @RequestParam("scheduleId") int scheduleId) {

        Users user = (Users) httpSession.getAttribute("LoggedIn");

        if (user == null) {
            return "redirect:/login";
        }

        if (!isAdmin(user)) {
            model.addAttribute("error", "Bạn không có quyền thực hiện hành động này!");
            return "403";
        }

        if (bindingResult.hasErrors()) {
            List<Plane> planes = iFlightService.getAllFlights();
            List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();
            model.addAttribute("planes", planes);
            model.addAttribute("flightSchedules", flightSchedules);
            model.addAttribute("schedule", schedule);
            return "editSchedule";
        }

        if (schedule.getTakeOffTime() != null && schedule.getLandTime() != null) {
            if (!schedule.getTakeOffTime().isBefore(schedule.getLandTime())) {
                List<Plane> planes = iFlightService.getAllFlights();
                List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();
                model.addAttribute("planes", planes);
                model.addAttribute("flightSchedules", flightSchedules);
                model.addAttribute("schedule", schedule);
                model.addAttribute("timeError", "⛔ Thời gian cất cánh phải trước thời gian hạ cánh!");
                return "editSchedule";
            }
        }

        Plane plane = iFlightService.getFlightById(planeId);
        FlightSchedule flightSchedule = iFlightScheduleService.getScheduleById(scheduleId);

        schedule.setPlane(plane);
        schedule.setFlightSchedule(flightSchedule);

        iFlightSchedulePlaneService.add(schedule);
        return "redirect:/schedule/admin";
    }


    @GetMapping("/admin/users")
    public ModelAndView showUserList(HttpSession session) {
        Users user = (Users) session.getAttribute("LoggedIn");
        ModelAndView mv = new ModelAndView();
        mv = authorizeAdmin(mv, user);

        if (!Objects.equals(mv.getViewName(), null)) {
            return mv;
        }

        List<Users> users = iUsersService.getAllUsers()
                .stream()
                .filter(users1 -> !users1.getRoleUser().equals(RoleUsers.ADMIN))
                .toList();
        mv.addObject("admin", user);
        mv.addObject("users", users);
        mv.setViewName("admin_manage-user");
        return mv;
    }

    @GetMapping("/payment")
    public ModelAndView viewPayments(HttpSession session) {
        Users user = (Users) session.getAttribute("LoggedIn");
        ModelAndView mv = new ModelAndView();
        mv = authorizeAdmin(mv, user);
        if (!Objects.equals(mv.getViewName(), null)) {
            return mv;
        }


        List<Payment> payments = iPaymentService.getAllPayments();
        mv.addObject("payments", payments);
        mv.addObject("admin", user);
        mv.setViewName("admin_payment-tracking");
        return mv;
    }

    public ModelAndView authorizeAdmin(ModelAndView mv, Users admin) {

        if (admin == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        if (!isAdmin(admin)) {
            mv.setViewName("403");
            mv.addObject("error", "Bạn không có quyền truy cập trang này!");
            return mv;
        }
        return mv;
    }
}
