package sum25.se.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sum25.se.entity.*;
import sum25.se.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    IBookingService iBookingService;
    @Autowired
    IFlightService iFlightService;
    @Autowired
    IPassengerInfoService iPassengerInfoService;
    @Autowired
    private IFlightSchedulePlaneService iFlightSchedulePlaneService;
    @Autowired
    VNPayService vnPayService;

    @GetMapping("/create")
    public String showBookingForm(@RequestParam(required = false) Integer flightId,
                                  Model model,
                                  HttpSession session) {
        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/main";
        }
        System.out.println("show booking create...");
        // Lấy danh sách chuyến bay để chọn (nếu người dùng muốn đổi)
        model.addAttribute("schedule_plane", iFlightSchedulePlaneService.findAll());
        model.addAttribute("user", user.getFullName());
        // Nếu có flightId -> Lấy chuyến bay cụ thể để pre-fill form
        if (flightId != null) {
            FlightSchedule_Plane flight = iFlightService.getFlightPlaneByFlightId(flightId);
            model.addAttribute("selectedFlight", flight);
        }

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

        FlightSchedule_Plane schedule_plane = iFlightSchedulePlaneService.findById(flightId);

        // Lưu Booking trước
        Booking savedBooking = iBookingService.createBooking(
                seatClass,
                totalPrice,
                user,
                schedule_plane
        );

        // Tạo PassengerInfo với tên hành khách
        PassengerInfo passengerInfo = new PassengerInfo();
        passengerInfo.setFullName(passengerName);
        if (passengerName.equalsIgnoreCase(user.getFullName())){
            passengerInfo.setPassportNumber(user.getPassportNumber());
            passengerInfo.setDateOfBirth(user.getDateOfBirth());
        }
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
            return "redirect:/main";
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
                return "redirect:/main";
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
                passengerInfo.setDateOfBirth(LocalDate.parse(dateOfBirthStr));
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

    @PostMapping("/update-and-payment/{id}")
    public String updateAndPayment(
            @PathVariable Integer id,
            @ModelAttribute("passengerInfo") PassengerInfo passengerInfo, 
            HttpSession session,
            Model model) {

        Users user = (Users) session.getAttribute("LoggedIn");
        if (user == null) {
            return "redirect:/login";
        }

        Booking booking = iBookingService.getBookingById(id);
        if (booking == null) {
            model.addAttribute("error", "Không tìm thấy vé đặt!");
            return "redirect:/booking/list";
        }

        if (booking.getStatus() != null && !booking.getStatus().toString().equals("PENDING")) {
            model.addAttribute("error", "Vé này đã được thanh toán hoặc đã bị hủy!");
            return "redirect:/booking/list";
        }

        if (!booking.getUsers().getUserId().equals(user.getUserId())) {
            model.addAttribute("error", "Bạn không có quyền thực hiện hành động này!");
            return "redirect:/booking/list";
        }

        try {
            passengerInfo.setBooking(booking);

            if (passengerInfo.getPassengerId() != null) {
                iPassengerInfoService.updatePassenger(passengerInfo.getPassengerId(), passengerInfo);
            } else {
                iPassengerInfoService.createPassenger(passengerInfo);
            }

            String paymentUrl = vnPayService.createPaymentUrl(booking);
            return "redirect:" + paymentUrl;

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "❌ Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("booking", booking);
            model.addAttribute("passengerInfo", passengerInfo);
            return "booking_edit";
        }
    }

    @GetMapping("/payment-return")
    public String paymentReturn(@RequestParam Map<String, String> allParams,
                                HttpSession session,
                                Model model) {
        try {
            System.out.println("📥 VNPay callback received");

            boolean isValid = vnPayService.validateCallback(allParams);

            if (!isValid) {
                System.out.println(" Invalid signature from VNPay");
                model.addAttribute("error", "Chữ ký không hợp lệ!");
                return "payment_failed";
            }

            String vnpResponseCode = allParams.get("vnp_ResponseCode");
            String vnpTransactionStatus = allParams.get("vnp_TransactionStatus");

            System.out.println("📊 Response Code: " + vnpResponseCode);
            System.out.println("📊 Transaction Status: " + vnpTransactionStatus);

            if ("00".equals(vnpResponseCode) && "00".equals(vnpTransactionStatus)) {
                String txnRef = allParams.get("vnp_TxnRef");
                Integer bookingId = Integer.parseInt(txnRef.split("_")[0]);

                System.out.println("✅ Payment successful for Booking ID: " + bookingId);

                Booking booking = iBookingService.getBookingById(bookingId);
                if (booking != null) {

                    booking.setStatus(StatusBooking.COMPLETED);
                    iBookingService.updateBooking(bookingId, booking);

                    System.out.println("✅ Booking status updated to COMPLETED");

                    model.addAttribute("message", "Thanh toán thành công!");
                    model.addAttribute("booking", booking);
                    return "payment_success";
                } else {
                    System.out.println(" Booking not found: " + bookingId);
                    model.addAttribute("error", "Không tìm thấy đơn đặt vé!");
                    return "payment_failed";
                }
            } else {
                System.out.println(" Payment failed with code: " + vnpResponseCode);
                model.addAttribute("error", "Giao dịch thất bại hoặc đã bị hủy!");
                model.addAttribute("responseCode", vnpResponseCode);
                return "payment_failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error processing payment callback: " + e.getMessage());
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "payment_failed";
        }
    }

}
