package sum25.se.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Import các entity
import sum25.se.entity.*;
import sum25.se.entity.Plane;


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
            admin.setRoleUser(RoleUsers.ADMIN);

            Users demoUser = new Users();
            demoUser.setFullName("Demo User");
            demoUser.setEmail("user@skyticket.com");
            demoUser.setPassword("user123");
            demoUser.setPhone("0987654321");
            demoUser.setPassportNumber("VN0000002");
            demoUser.setDateOfBirth(LocalDate.of(1995, 5, 15)); // 1995-05-15
            demoUser.setRoleUser(RoleUsers.USER);

            iUsersService.createUser(admin);
            iUsersService.createUser(demoUser);

            System.out.println("✅ Default users initialized successfully!");
        } else {
            System.out.println("ℹ️ Users already exist — skipping initialization.");
        }
        Airport tsn = createAndSaveAirport("Sân bay Tân Sơn Nhất", "SGN", "TP. Hồ Chí Minh", airportService);
        Airport nba = createAndSaveAirport("Sân bay Nội Bài", "HAN", "Hà Nội", airportService);
        Airport dad = createAndSaveAirport("Sân bay Đà Nẵng", "DAD", "Đà Nẵng", airportService);

            Airport cxr = createAndSaveAirport("Sân bay Cam Ranh", "CXR", "Khánh Hòa", airportService);
            Airport pqc = createAndSaveAirport("Sân bay Phú Quốc", "PQC", "Kiên Giang", airportService);
            Airport vdo = createAndSaveAirport("Sân bay Vân Đồn", "VDO", "Quảng Ninh", airportService);
            Airport hph = createAndSaveAirport("Sân bay Cát Bi", "HPH", "Hải Phòng", airportService);
            Airport vii = createAndSaveAirport("Sân bay Vinh", "VII", "Nghệ An", airportService);
            Airport hui = createAndSaveAirport("Sân bay Phú Bài", "HUI", "Thừa Thiên Huế", airportService);
            Airport vca = createAndSaveAirport("Sân bay Cần Thơ", "VCA", "Cần Thơ", airportService);
            Airport dli = createAndSaveAirport("Sân bay Liên Khương", "DLI", "Lâm Đồng", airportService);
            Airport bmv = createAndSaveAirport("Sân bay Buôn Ma Thuột", "BMV", "Đắk Lắk", airportService);


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


            Plane vna_SGN_HAN = new Plane();
            vna_SGN_HAN.setPlaneModel("Vietnam Airlines"); // HÃNG MÁY BAY
            vna_SGN_HAN.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0)); // 8:00
            vna_SGN_HAN.setDuration(125);
            vna_SGN_HAN.setStatus("Scheduled");
            vna_SGN_HAN.setAirport(tsn);
            vna_SGN_HAN = flightService.addFlight(vna_SGN_HAN);

            // 2. VietJet Air: HAN -> SGN
            Plane vjet_HAN_SGN = new Plane();
            vjet_HAN_SGN.setPlaneModel("VietJet Air"); // HÃNG MÁY BAY
            vjet_HAN_SGN.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30)); // 10:30
            vjet_HAN_SGN.setDuration(130);
            vjet_HAN_SGN.setStatus("Scheduled");
            vjet_HAN_SGN.setAirport(nba);
            vjet_HAN_SGN = flightService.addFlight(vjet_HAN_SGN);

            // 3. Bamboo Airways: SGN -> HAN
            Plane bamboo_SGN_HAN = new Plane();
            bamboo_SGN_HAN.setPlaneModel("Bamboo Airways"); // HÃNG MÁY BAY
            bamboo_SGN_HAN.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0)); // 14:00
            bamboo_SGN_HAN.setDuration(120);
            bamboo_SGN_HAN.setStatus("Scheduled");
            bamboo_SGN_HAN.setAirport(tsn);
            bamboo_SGN_HAN = flightService.addFlight(bamboo_SGN_HAN);

            // 4. Pacific Airlines: HAN -> DAD
            Plane pacific_HAN_DAD = new Plane();
            pacific_HAN_DAD.setPlaneModel("Pacific Airlines"); // HÃNG MÁY BAY
            pacific_HAN_DAD.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(16).withMinute(30)); // 16:30
            pacific_HAN_DAD.setDuration(90);
            pacific_HAN_DAD.setStatus("Scheduled");
            pacific_HAN_DAD.setAirport(nba);
            pacific_HAN_DAD = flightService.addFlight(pacific_HAN_DAD);

            // 5. Vietravel Airlines: DAD -> SGN
            Plane vietravel_DAD_SGN = new Plane();
            vietravel_DAD_SGN.setPlaneModel("Vietravel Airlines"); // HÃNG MÁY BAY
            vietravel_DAD_SGN.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0)); // 18:00
            vietravel_DAD_SGN.setDuration(100);
            vietravel_DAD_SGN.setStatus("Scheduled");
            vietravel_DAD_SGN.setAirport(dad); // Sân bay đi là Đà Nẵng
            vietravel_DAD_SGN = flightService.addFlight(vietravel_DAD_SGN);

            // 6. Vietnam Airlines: HAN -> SGN (Thêm một chuyến khác của VNA)
            Plane vna_HAN_SGN = new Plane();
            vna_HAN_SGN.setPlaneModel("Vietnam Airlines"); // HÃNG MÁY BAY
            vna_HAN_SGN.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(20).withMinute(0)); // 20:00
            vna_HAN_SGN.setDuration(135);
            vna_HAN_SGN.setStatus("Scheduled");
            vna_HAN_SGN.setAirport(nba);
            vna_HAN_SGN = flightService.addFlight(vna_HAN_SGN);

            // --- KHỞI TẠO 6 LIÊN KẾT CHUYẾN BAY (FlightSchedule_Plane) ---

            // 1. VNA SGN->HAN Economy
            FlightSchedule_Plane link1 = new FlightSchedule_Plane();
            link1.setPlane(vna_SGN_HAN);
            link1.setFlightSchedule(econSeatTemplate);
            link1.setTakeOff("SGN");
            link1.setLand("HAN");
            link1.setTakeOffTime(vna_SGN_HAN.getDepartureTime());
            link1.setLandTime(vna_SGN_HAN.getDepartureTime().plusMinutes(vna_SGN_HAN.getDuration()));
            iFlightSchedulePlaneService.add(link1);

            // 2. VNA SGN->HAN Business
            FlightSchedule_Plane link2 = new FlightSchedule_Plane();
            link2.setPlane(vna_SGN_HAN);
            link2.setFlightSchedule(bizSeatTemplate);
            link2.setTakeOff("SGN");
            link2.setLand("HAN");
            link2.setTakeOffTime(vna_SGN_HAN.getDepartureTime());
            link2.setLandTime(vna_SGN_HAN.getDepartureTime().plusMinutes(vna_SGN_HAN.getDuration()));
            iFlightSchedulePlaneService.add(link2);

            // 3. VJ HAN->SGN Economy
            FlightSchedule_Plane link3 = new FlightSchedule_Plane();
            link3.setPlane(vjet_HAN_SGN);
            link3.setFlightSchedule(econSeatTemplate);
            link3.setTakeOff("HAN");
            link3.setLand("SGN");
            link3.setTakeOffTime(vjet_HAN_SGN.getDepartureTime());
            link3.setLandTime(vjet_HAN_SGN.getDepartureTime().plusMinutes(vjet_HAN_SGN.getDuration()));
            iFlightSchedulePlaneService.add(link3);

            // 4. QH SGN->HAN Economy
            FlightSchedule_Plane link4 = new FlightSchedule_Plane();
            link4.setPlane(bamboo_SGN_HAN);
            link4.setFlightSchedule(econSeatTemplate);
            link4.setTakeOff("SGN");
            link4.setLand("HAN");
            link4.setTakeOffTime(bamboo_SGN_HAN.getDepartureTime());
            link4.setLandTime(bamboo_SGN_HAN.getDepartureTime().plusMinutes(bamboo_SGN_HAN.getDuration()));
            iFlightSchedulePlaneService.add(link4);

            // 5. BL HAN->DAD Economy
            FlightSchedule_Plane link5 = new FlightSchedule_Plane();
            link5.setPlane(pacific_HAN_DAD);
            link5.setFlightSchedule(econSeatTemplate);
            link5.setTakeOff("HAN");
            link5.setLand("DAD");
            link5.setTakeOffTime(pacific_HAN_DAD.getDepartureTime());
            link5.setLandTime(pacific_HAN_DAD.getDepartureTime().plusMinutes(pacific_HAN_DAD.getDuration()));
            iFlightSchedulePlaneService.add(link5);

            // 6. VU DAD->SGN Economy
            FlightSchedule_Plane link6 = new FlightSchedule_Plane();
            link6.setPlane(vietravel_DAD_SGN);
            link6.setFlightSchedule(econSeatTemplate);
            link6.setTakeOff("DAD");
            link6.setLand("SGN");
            link6.setTakeOffTime(vietravel_DAD_SGN.getDepartureTime());
            link6.setLandTime(vietravel_DAD_SGN.getDepartureTime().plusMinutes(vietravel_DAD_SGN.getDuration()));
            iFlightSchedulePlaneService.add(link6);

            // 7. VNA HAN->SGN Business
            FlightSchedule_Plane link7 = new FlightSchedule_Plane();
            link7.setPlane(vna_HAN_SGN);
            link7.setFlightSchedule(bizSeatTemplate);
            link7.setTakeOff("HAN");
            link7.setLand("SGN");
            link7.setTakeOffTime(vna_HAN_SGN.getDepartureTime());
            link7.setLandTime(vna_HAN_SGN.getDepartureTime().plusMinutes(vna_HAN_SGN.getDuration()));
            iFlightSchedulePlaneService.add(link7);

        // 7. VietJet Air: SGN -> PQC
        Plane vjet_SGN_PQC = createAndSavePlane("VietJet Air", tsn, 5, 0, 75, flightService);
        // 8. Vietnam Airlines: HAN -> CXR
        Plane vna_HAN_CXR = createAndSavePlane("Vietnam Airlines", nba, 7, 0, 110, flightService);
        // 9. Bamboo Airways: VDO -> SGN
        Plane bamboo_VDO_SGN = createAndSavePlane("Bamboo Airways", vdo, 9, 30, 150, flightService);
        // 10. Pacific Airlines: DAD -> HPH
        Plane pacific_DAD_HPH = createAndSavePlane("Pacific Airlines", dad, 12, 45, 95, flightService);
        // 11. Vietravel Airlines: SGN -> VCA
        Plane vietravel_SGN_VCA = createAndSavePlane("Vietravel Airlines", tsn, 15, 15, 60, flightService);
        // 12. VietJet Air: VII -> DLI
        Plane vjet_VII_DLI = createAndSavePlane("VietJet Air", vii, 18, 0, 105, flightService);


        // --- THÊM 6 LIÊN KẾT CHUYẾN BAY MỚI (FlightSchedule_Plane) ---

        // 7. VJ SGN->PQC Economy
        createAndSaveFlightLink(vjet_SGN_PQC, econSeatTemplate, "SGN", "PQC", iFlightSchedulePlaneService);
        // 8. VNA HAN->CXR Business
        createAndSaveFlightLink(vna_HAN_CXR, bizSeatTemplate, "HAN", "CXR", iFlightSchedulePlaneService);
        // 9. QH VDO->SGN Economy
        createAndSaveFlightLink(bamboo_VDO_SGN, econSeatTemplate, "VDO", "SGN", iFlightSchedulePlaneService);
        // 10. BL DAD->HPH Economy
        createAndSaveFlightLink(pacific_DAD_HPH, econSeatTemplate, "DAD", "HPH", iFlightSchedulePlaneService);
        // 11. VU SGN->VCA Business
        createAndSaveFlightLink(vietravel_SGN_VCA, bizSeatTemplate, "SGN", "VCA", iFlightSchedulePlaneService);
        // 12. VJ VII->DLI Economy
        createAndSaveFlightLink(vjet_VII_DLI, econSeatTemplate, "VII", "DLI", iFlightSchedulePlaneService);

            System.out.println("✅ Default data for new entities initialized successfully!");
        }

    private Airport createAndSaveAirport(String name, String code, String location, IAirportService service) {
        Airport airport = new Airport();
        airport.setAirportName(name);
        airport.setCode(code);
        airport.setLocation(location);
        return service.addAirport(airport);
    }

    private Plane createAndSavePlane(String model, Airport departureAirport, int hour, int minute, int duration, IFlightService service) {
        Plane plane = new Plane();
        plane.setPlaneModel(model);
        plane.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(hour).withMinute(minute));
        plane.setDuration(duration);
        plane.setStatus("Scheduled");
        plane.setAirport(departureAirport);
        return service.addFlight(plane);
    }

    private void createAndSaveFlightLink(Plane plane, FlightSchedule schedule, String takeOffCode, String landCode, IFlightSchedulePlaneService service) {
        FlightSchedule_Plane link = new FlightSchedule_Plane();
        link.setPlane(plane);
        link.setFlightSchedule(schedule);
        link.setTakeOff(takeOffCode);
        link.setLand(landCode);
        link.setTakeOffTime(plane.getDepartureTime());
        link.setLandTime(plane.getDepartureTime().plusMinutes(plane.getDuration()));
        service.add(link);
    }
}

