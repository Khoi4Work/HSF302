package sum25.se.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sum25.se.entity.FlightSchedule_Plane;
import sum25.se.service.IFlightSchedulePlaneService;

import java.util.List;

@Controller
public class FlightController {
    @Autowired
    IFlightSchedulePlaneService iFlightSchedulePlaneService;

    @GetMapping("/flights")
    public String showFlightList(Model model) {
        List<FlightSchedule_Plane> flightList = iFlightSchedulePlaneService.findAll();

        model.addAttribute("flights", flightList);

        return "flights";
    }
}
