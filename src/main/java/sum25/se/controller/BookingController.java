package sum25.se.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sum25.se.entity.Booking;
import sum25.se.entity.PassengerInfo;
import sum25.se.entity.Plane;
import sum25.se.entity.Users;
import sum25.se.service.IBookingService;
import sum25.se.service.IFlightService;
import sum25.se.service.IPassengerInfoService;
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
    @Autowired
    IPassengerInfoService iPassengerInfoService;

    // Hiển thị form đặt vé
    @GetMapping("/create")
    public String showBookingForm(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/main";
        }
        model.addAttribute("flights", iFlightService.getAllFlights());
        return "booking_form";
    }

    // Xử lý khi người dùng submit form
    @PostMapping("/create")
    public String processBookingForm(
            @RequestParam("passengerName") String passengerName,
            @RequestParam("flightId") Integer flightId,
            @RequestParam("seatClass") String seatClass,
            @RequestParam("totalPrice") Integer totalPrice,
            HttpSession session,
            Model model) {

        // Kiểm tra user đăng nhập
        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/main";
        }

        // Lấy Plane từ flightId
        Plane plane = iFlightService.getFlightById(flightId);
        if (plane == null) {
            model.addAttribute("error", "Chuyến bay không tồn tại!");
            model.addAttribute("flights", iFlightService.getAllFlights());
            return "booking_form";
        }


        // Lưu Booking trước
        Booking savedBooking = iBookingService.createBooking(
                seatClass,
                totalPrice,
                user,
                plane
        );

        // Tạo PassengerInfo với tên hành khách
        PassengerInfo passengerInfo = new PassengerInfo();
        passengerInfo.setFullName(passengerName);
        passengerInfo.setBooking(savedBooking);

        // Lưu PassengerInfo
        iPassengerInfoService.createPassenger(passengerInfo);

        model.addAttribute("message", "Đặt vé thành công!");
        return "booking_success"; // → resources/templates/booking_success.html
    }

    // Hiển thị danh sách booking của user đang đăng nhập
    @GetMapping("/list")
    public String showUserBookings(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy danh sách booking của user đang đăng nhập
        List<Booking> bookings = iBookingService.getBookingsByUser(user);
        model.addAttribute("bookings", bookings);
        return "booking_list"; // → resources/templates/booking_list.html
    }

    // Hiển thị form cập nhật thông tin booking
    @GetMapping("/edit/{id}")
    public String showEditBookingForm(@PathVariable Integer id, HttpSession session, Model model) {
        try {
            Users user = (Users) session.getAttribute("LoggedIn");
            if (user == null) {
                return "redirect:/login";
            }

            // Lấy booking
            Booking booking = iBookingService.getBookingById(id);
            if (booking == null) {
                model.addAttribute("error", "Không tìm thấy vé đặt!");
                List<Booking> bookings = iBookingService.getBookingsByUser(user);
                model.addAttribute("bookings", bookings);
                return "booking_list";
            }

            // Kiểm tra quyền sở hữu - kiểm tra từng bước để tránh NullPointerException
            if (booking.getUsers() == null) {
                model.addAttribute("error", "Vé đặt này không có thông tin người dùng!");
                List<Booking> bookings = iBookingService.getBookingsByUser(user);
                model.addAttribute("bookings", bookings);
                return "booking_list";
            }

            if (booking.getUsers().getUserId() == null || user.getUserId() == null) {
                model.addAttribute("error", "Không thể xác định quyền truy cập!");
                List<Booking> bookings = iBookingService.getBookingsByUser(user);
                model.addAttribute("bookings", bookings);
                return "booking_list";
            }

            if (!booking.getUsers().getUserId().equals(user.getUserId())) {
                model.addAttribute("error", "Bạn không có quyền cập nhật vé này!");
                List<Booking> bookings = iBookingService.getBookingsByUser(user);
                model.addAttribute("bookings", bookings);
                return "booking_list";
            }

            // Kiểm tra status - chỉ cho phép cập nhật khi PENDING
            if (booking.getStatus() != null && !booking.getStatus().toString().equals("PENDING")) {
                model.addAttribute("error", "Chỉ có thể cập nhật vé đang ở trạng thái Đang chờ!");
                List<Booking> bookings = iBookingService.getBookingsByUser(user);
                model.addAttribute("bookings", bookings);
                return "booking_list";
            }

            // Lấy PassengerInfo
            PassengerInfo passengerInfo = null;
            if (booking.getPassengerInfos() != null && !booking.getPassengerInfos().isEmpty()) {
                passengerInfo = booking.getPassengerInfos().get(0);
            } else {
                // Tạo PassengerInfo mới nếu chưa có
                passengerInfo = new PassengerInfo();
                passengerInfo.setBooking(booking);
            }

            model.addAttribute("booking", booking);
            model.addAttribute("passengerInfo", passengerInfo);
            return "booking_edit";
        } catch (Exception e) {
            // Log lỗi để debug
            e.printStackTrace();
            // Xử lý lỗi và redirect về danh sách
            Users user = (Users) session.getAttribute("LoggedIn");
            if (user != null) {
                try {
                    model.addAttribute("error", "Có lỗi xảy ra khi tải trang: " + e.getMessage());
                    List<Booking> bookings = iBookingService.getBookingsByUser(user);
                    model.addAttribute("bookings", bookings);
                    return "booking_list";
                } catch (Exception ex) {
                    // Nếu có lỗi khi load danh sách, redirect về login
                    return "redirect:/login";
                }
            } else {
                return "redirect:/login";
            }
        }
    }

    // Xử lý cập nhật thông tin booking
    @PostMapping("/edit/{id}")
    public String processEditBooking(
            @PathVariable Integer id,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "passportNumber", required = false) String passportNumber,
            @RequestParam(value = "dateOfBirth", required = false) String dateOfBirthStr,
            HttpSession session,
            Model model) {

        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy booking
        Booking booking = iBookingService.getBookingById(id);
        if (booking == null) {
            model.addAttribute("error", "Không tìm thấy vé đặt!");
            return "redirect:/booking/list";
        }

        // Kiểm tra quyền sở hữu
        if (!booking.getUsers().getUserId().equals(user.getUserId())) {
            model.addAttribute("error", "Bạn không có quyền cập nhật vé này!");
            return "redirect:/booking/list";
        }

        // Không cập nhật chuyến bay, hạng ghế, và giá - giữ nguyên giá trị cũ
        // Chỉ cập nhật thông tin PassengerInfo
        PassengerInfo passengerInfo = null;
        if (booking.getPassengerInfos() != null && !booking.getPassengerInfos().isEmpty()) {
            passengerInfo = booking.getPassengerInfos().get(0);
        } else {
            passengerInfo = new PassengerInfo();
            passengerInfo.setBooking(booking);
        }

        passengerInfo.setFullName(fullName);
        passengerInfo.setGender(gender);
        passengerInfo.setPassportNumber(passportNumber);

        // Xử lý dateOfBirth
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateOfBirth = sdf.parse(dateOfBirthStr);
                passengerInfo.setDateOfBirth(dateOfBirth);
            } catch (Exception e) {
                // Nếu không parse được, giữ nguyên giá trị cũ
            }
        }

        try {
            if (passengerInfo.getPassengerId() != null) {
                // Cập nhật PassengerInfo hiện có
                iPassengerInfoService.updatePassenger(passengerInfo.getPassengerId(), passengerInfo);
            } else {
                // Tạo PassengerInfo mới
                iPassengerInfoService.createPassenger(passengerInfo);
            }

            // Redirect về danh sách booking sau khi cập nhật thành công
            return "redirect:/booking/list";
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
            // Load lại booking từ database
            Booking refreshedBooking = iBookingService.getBookingById(id);
            if (refreshedBooking != null) {
                model.addAttribute("booking", refreshedBooking);
                PassengerInfo errorPassengerInfo = refreshedBooking.getPassengerInfos() != null && !refreshedBooking.getPassengerInfos().isEmpty()
                        ? refreshedBooking.getPassengerInfos().get(0) : new PassengerInfo();
                if (errorPassengerInfo.getPassengerId() == null) {
                    errorPassengerInfo.setBooking(refreshedBooking);
                }
                model.addAttribute("passengerInfo", errorPassengerInfo);
            } else {
                model.addAttribute("booking", booking);
                model.addAttribute("passengerInfo", passengerInfo);
            }
            return "booking_edit";
        }
    }

    // Hủy vé (xóa booking)
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Integer id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/login";
        }

        // Kiểm tra booking có thuộc user này không
        Booking booking = iBookingService.getBookingById(id);
        if (booking == null) {
            model.addAttribute("error", "Không tìm thấy vé đặt!");
            List<Booking> bookings = iBookingService.getBookingsByUser(user);
            model.addAttribute("bookings", bookings);
            return "booking_list";
        }

        // Kiểm tra quyền sở hữu
        if (booking.getUsers() == null || booking.getUsers().getUserId() == null ||
                user.getUserId() == null || !booking.getUsers().getUserId().equals(user.getUserId())) {
            model.addAttribute("error", "Bạn không có quyền hủy vé này!");
            List<Booking> bookings = iBookingService.getBookingsByUser(user);
            model.addAttribute("bookings", bookings);
            return "booking_list";
        }

        // Xóa vé (sẽ tự động xóa PassengerInfo và các liên kết liên quan)
        iBookingService.deleteBooking(id);
        return "redirect:/booking/list";
    }
}
