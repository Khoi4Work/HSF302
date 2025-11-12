package sum25.se.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sum25.se.entity.FlightSchedule_Plane;
import sum25.se.service.IAirportService;
import sum25.se.service.IFlightSchedulePlaneService;
import sum25.se.service.IFlightService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class FlightController {
    @Autowired
    IFlightSchedulePlaneService iFlightSchedulePlaneService;
    @Autowired
    private IFlightService iFlightService;
    @Autowired
    private IAirportService iAirportService;

    @GetMapping("/flights")
    public String showFlightList(Model model) {
        List<FlightSchedule_Plane> flightList = iFlightSchedulePlaneService.findAll();

        model.addAttribute("flights", flightList);

        return "flights";
    }

    @GetMapping("/flight/search")
    public String searchFlightList(@RequestParam("departure") String departure,
                                   @RequestParam("destination") String destination,
                                   @RequestParam("date")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam(value = "seatClass", required = false) String seatClass,
                                   @RequestParam("page") Integer page,
                                   Model model) {

        List<FlightSchedule_Plane> flights = iFlightService.searchFlights(departure, destination, date, seatClass);

        model.addAttribute("flights", flights);
        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        model.addAttribute("seatClass", seatClass);
        model.addAttribute("airports", iAirportService.getAllAirports());

        return switch (page) {
            case 1 -> "admin";
            case 2 -> "mainPage";
            case 3 -> "main";
            default -> "error";
        };
    }
}
