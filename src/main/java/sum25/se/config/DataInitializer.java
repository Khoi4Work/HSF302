package sum25.se.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Import các entity
import sum25.se.entity.*;
import sum25.se.entity.Plane;


import sum25.se.repository.IFlightSchedulePlaneRepository;
import sum25.se.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    IUsersService iUsersService;

    // ----- [MỚI] Thêm các Repository cần thiết -----
    @Autowired
    private IAirportService airportService;

    @Autowired
    private IFlightService flightService;

    @Autowired
    private IFlightScheduleService flightScheduleService;

    @Autowired
    private IFlightSchedulePlaneService iFlightSchedulePlaneService;
    // -------------------------------------------------


    @Override
    public void run(String... args) throws Exception {

        // --- 1. Khởi tạo Users ---
        // (Đã sửa lỗi năm sinh 90 -> 1990 và 95 -> 1995)
        if (iUsersService.getAllUsers().isEmpty()) {
            Users admin = new Users();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@skyticket.com");
            admin.setPassword("admin123");
            admin.setPhone("0123456789");
            admin.setPassportNumber("VN0000001");
            admin.setDateOfBirth(LocalDate.of(1990, 1, 1)); // 1990-01-01
            admin.setRoleUses(RoleUsers.ADMIN);

            Users demoUser = new Users();
            demoUser.setFullName("Demo User");
            demoUser.setEmail("user@skyticket.com");
            demoUser.setPassword("user123");
            demoUser.setPhone("0987654321");
            demoUser.setPassportNumber("VN0000002");
            demoUser.setDateOfBirth(LocalDate.of(1995, 5, 15)); // 1995-05-15
            demoUser.setRoleUses(RoleUsers.USER);

            iUsersService.createUser(admin);
            iUsersService.createUser(demoUser);

            System.out.println("✅ Default users initialized successfully!");
        } else {
            System.out.println("ℹ️ Users already exist — skipping initialization.");
        }
        if (airportService.getAiportByCode("SGN") == null) {
            Airport tsn = new Airport();
            tsn.setAirportName("Sân bay Tân Sơn Nhất");
            tsn.setCode("SGN");
            tsn.setLocation("Hồ Chí Minh, Việt Nam");
            tsn = airportService.addAirport(tsn);

            Airport nba = new Airport();
            nba.setAirportName("Sân bay Nội Bài");
            nba.setCode("HAN");
            nba.setLocation("Hà Nội, Việt Nam");
            nba = airportService.addAirport(nba);


            FlightSchedule econSeatTemplate = new FlightSchedule();
            econSeatTemplate.setSeatNumber(1); // Đây có thể là số thứ tự loại ghế
            econSeatTemplate.setSeatClass("Economy");
            econSeatTemplate.setPrice(1500000);
            econSeatTemplate.setStatus("Template"); // Trạng thái mẫu
            econSeatTemplate = flightScheduleService.addFlightSchedulte(econSeatTemplate);

            FlightSchedule bizSeatTemplate = new FlightSchedule();
            bizSeatTemplate.setSeatNumber(10); // Loại ghế số 10
            bizSeatTemplate.setSeatClass("Business");
            bizSeatTemplate.setPrice(3000000);
            bizSeatTemplate.setStatus("Template");
            bizSeatTemplate = flightScheduleService.addFlightSchedulte(bizSeatTemplate);


            Plane plane_SGN_HAN = new Plane();
            plane_SGN_HAN.setPlaneModel("Airbus A321");
            plane_SGN_HAN.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0)); // 8:00 sáng mai
            plane_SGN_HAN.setDuration(125);
            plane_SGN_HAN.setStatus("Scheduled");
            plane_SGN_HAN.setAirport(tsn);
            plane_SGN_HAN = flightService.addFlight(plane_SGN_HAN);

            Plane plane_HAN_SGN = new Plane();
            plane_HAN_SGN.setPlaneModel("Boeing 787");
            plane_HAN_SGN.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30)); // 10:30 sáng mai
            plane_HAN_SGN.setDuration(130);
            plane_HAN_SGN.setStatus("Scheduled");
            plane_HAN_SGN.setAirport(nba); // Sân bay đi là Nội Bài
            plane_HAN_SGN = flightService.addFlight(plane_HAN_SGN);




            FlightSchedule_Plane link1 = new FlightSchedule_Plane();
            link1.setPlane(plane_SGN_HAN);
            link1.setFlightSchedule(econSeatTemplate);
            link1.setTakeOff("SGN");
            link1.setLand("HAN");
            link1.setTakeOffTime(plane_SGN_HAN.getDepartureTime());
            link1.setLandTime(plane_SGN_HAN.getDepartureTime().plusMinutes(plane_SGN_HAN.getDuration()));
            iFlightSchedulePlaneService.add(link1);

            FlightSchedule_Plane link2 = new FlightSchedule_Plane();
            link2.setPlane(plane_SGN_HAN);
            link2.setFlightSchedule(bizSeatTemplate);
            link2.setTakeOff("SGN");
            link2.setLand("HAN");
            link2.setTakeOffTime(plane_SGN_HAN.getDepartureTime());
            link2.setLandTime(plane_SGN_HAN.getDepartureTime().plusMinutes(plane_SGN_HAN.getDuration()));
            iFlightSchedulePlaneService.add(link2);

            FlightSchedule_Plane link3 = new FlightSchedule_Plane();
            link3.setPlane(plane_HAN_SGN);
            link3.setFlightSchedule(econSeatTemplate);
            link3.setTakeOff("HAN");
            link3.setLand("SGN");
            link3.setTakeOffTime(plane_HAN_SGN.getDepartureTime());
            link3.setLandTime(plane_HAN_SGN.getDepartureTime().plusMinutes(plane_HAN_SGN.getDuration()));
            iFlightSchedulePlaneService.add(link3);

            System.out.println("✅ Default data for new entities initialized successfully!");
        }
    }
}