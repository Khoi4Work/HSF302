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

    private boolean isAdmin(Users user) {
        return user != null && user.getRoleUser() == RoleUsers.ADMIN;
    }

    @GetMapping("/admin")
    public ModelAndView showAdminPage(HttpSession httpSession) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();

        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        if (!isAdmin(user)) {
            modelAndView.setViewName("403"); // Trang Forbidden
            modelAndView.addObject("error", "Bạn không có quyền truy cập trang này!");
            return modelAndView;
        }

        modelAndView.addObject("user", user);

        Boolean loginSuccess = (Boolean) httpSession.getAttribute("loginSuccess");
        if (loginSuccess != null && loginSuccess) {
            modelAndView.addObject("loginSuccess", true);
            httpSession.removeAttribute("loginSuccess");
        }

        List<FlightSchedule_Plane> schedules = iFlightSchedulePlaneService.findAll();
        modelAndView.addObject("schedules", schedules);
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
    public ModelAndView showAddNewSchedule(HttpSession httpSession) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();


        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }


        if (!isAdmin(user)) {
            modelAndView.setViewName("403");
            modelAndView.addObject("error", "Bạn không có quyền truy cập trang này!");
            return modelAndView;
        }

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
    public ModelAndView showEditSchedule(HttpSession httpSession, @PathVariable int id) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();
        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }


        if (!isAdmin(user)) {
            modelAndView.setViewName("403");
            modelAndView.addObject("error", "Bạn không có quyền truy cập trang này!");
            return modelAndView;
        }

        List<Plane> planes = iFlightService.getAllFlights();
        List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();
        FlightSchedule_Plane schedule = iFlightSchedulePlaneService.findById(id);

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
        Users loggedIn = (Users) session.getAttribute("LoggedIn");
        ModelAndView mv = new ModelAndView();

        if (loggedIn == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        if (!isAdmin(loggedIn)) {
            mv.setViewName("403");
            mv.addObject("error", "Bạn không có quyền truy cập trang này!");
            return mv;
        }

        List<Users> users = iUsersService.getAllUsers();
        mv.addObject("users", users);
        mv.setViewName("admin_manage-user");
        return mv;
    }
}
