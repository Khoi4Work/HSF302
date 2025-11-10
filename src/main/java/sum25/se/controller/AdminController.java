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
    IFlightSchedulePlaneService iFlightSchedulePlaneService;
    @Autowired
    IFlightService iFlightService;
    @Autowired
    IFlightScheduleService iFlightScheduleService;

    @GetMapping("/admin")
    public ModelAndView showAdminPage(HttpSession httpSession) {
        Users users = (Users) httpSession.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();

        if (users == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        // Kiểm tra nếu vừa login thành công
        Boolean loginSuccess = (Boolean) httpSession.getAttribute("loginSuccess");
        if (loginSuccess != null && loginSuccess) {
            modelAndView.addObject("loginSuccess", true);
            modelAndView.addObject("user", users);
            httpSession.removeAttribute("loginSuccess"); // Xóa sau khi đã hiển thị
        }

        List<FlightSchedule_Plane> schedules = iFlightSchedulePlaneService.findAll();
        modelAndView.addObject("schedules", schedules);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/deleteSchedule/{id}")
    public String deleteSchedule(HttpSession httpSession, @PathVariable int id) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");

        if (user == null) {
            return "redirect:/login";
        }

        iFlightSchedulePlaneService.deleteSchedule(id);
        return "redirect:/schedule/admin";
    }


    @GetMapping("/addSchedule")
    public ModelAndView addNewSchedule(HttpSession httpSession) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");
        if(user == null){
            return new ModelAndView("redirect:/main");
        } else if (!user.getRoleUser().equals(RoleUsers.ADMIN)) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView modelAndView = new ModelAndView();

        List<Plane> planes = iFlightService.getAllFlights();
        List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();

        modelAndView.addObject("planes", planes);
        modelAndView.addObject("flightSchedules", flightSchedules);
        modelAndView.addObject("schedule", new FlightSchedule_Plane());
        modelAndView.setViewName("addSchedule");
        return modelAndView;
    }

    @PostMapping("/addSchedule")
    public String addNewSchedule(@Valid @ModelAttribute("schedule") FlightSchedule_Plane schedule,
                                 BindingResult bindingResult,
                                 HttpSession httpSession,
                                 Model model,
                                 @RequestParam("planeId") int planeId,
                                 @RequestParam("scheduleId") int scheduleId) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");
        if(user == null){
            return "redirect:/main";
        } else if (!user.getRoleUser().equals(RoleUsers.ADMIN)) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            List<Plane> planes = iFlightService.getAllFlights();
            List<FlightSchedule> flightSchedules = iFlightScheduleService.getAllSchedules();
            model.addAttribute("planes", planes);
            model.addAttribute("flightSchedules", flightSchedules);
            model.addAttribute("schedule", schedule);
            return "addSchedule";
        }

        Plane plane = iFlightService.getFlightById(planeId);
        FlightSchedule flightSchedule = iFlightScheduleService.getScheduleById(scheduleId);

        schedule.setPlane(plane);
        schedule.setFlightSchedule(flightSchedule);

        iFlightSchedulePlaneService.add(schedule);
        return "redirect:/schedule/admin";
    }

    @GetMapping("/editSchedule/{id}")
    public ModelAndView editSchedule(HttpSession httpSession, @PathVariable int id) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");
        ModelAndView modelAndView = new ModelAndView();

        if (user == null) {
            modelAndView.setViewName("redirect:/main");
            return modelAndView;
        } else if (!user.getRoleUser().equals(RoleUsers.ADMIN)) {
            return new ModelAndView("403");
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
    public String editSchedule(@Valid @ModelAttribute("schedule") FlightSchedule_Plane schedule,
                               BindingResult bindingResult,
                               HttpSession httpSession,
                               Model model,
                               @RequestParam("planeId") int planeId,
                               @RequestParam("scheduleId") int scheduleId) {
        Users user = (Users) httpSession.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/main";
        } else if (!user.getRoleUser().equals(RoleUsers.ADMIN)) {
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

        Plane plane = iFlightService.getFlightById(planeId);
        FlightSchedule flightSchedule = iFlightScheduleService.getScheduleById(scheduleId);

        schedule.setPlane(plane);
        schedule.setFlightSchedule(flightSchedule);

        iFlightSchedulePlaneService.add(schedule);
        return "redirect:/schedule/admin";
    }
}
