package sum25.se.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sum25.se.entity.Booking;
import sum25.se.service.BookingServiceImpl;
import sum25.se.service.IBookingService;
import sum25.se.service.IFlightService;
import sum25.se.service.IUsersService;

import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    IBookingService iBookingService;
    @Autowired
    IFlightService iFlightService;
    @Autowired
    IUsersService iUsersService;

    // Hiển thị form đặt vé
    @GetMapping("/create")
    public String showBookingForm(Model model) {
        model.addAttribute("flights", iFlightService.getAllFlights());
        model.addAttribute("users", iUsersService.getAllUsers());
        model.addAttribute("booking", new Booking());
        return "booking_form"; // → resources/templates/booking_form.html
    }

    // Xử lý khi người dùng submit form
    @PostMapping("/create")
    public String processBookingForm(@ModelAttribute Booking booking, Model model) {
        iBookingService.createBooking(booking);
        model.addAttribute("message", "Đặt vé thành công!");
        return "booking_success"; // → resources/templates/booking_success.html
    }

    // Hiển thị danh sách tất cả booking
    @GetMapping("/list")
    public String showAllBookings(Model model) {
        List<Booking> bookings = iBookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "booking_list"; // → resources/templates/booking_list.html
    }
}
