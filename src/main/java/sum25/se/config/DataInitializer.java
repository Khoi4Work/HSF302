package sum25.se.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sum25.se.entity.*;
import sum25.se.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DataInitializer - Khởi tạo dữ liệu mẫu cho hệ thống đặt vé máy bay
 * Chỉ chạy MỘT LẦN khi database còn trống
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUsersService iUsersService;

    @Autowired
    private IAirportService airportService;

    @Autowired
    private IFlightService flightService;

    @Autowired
    private IFlightScheduleService flightScheduleService;

    @Autowired
    private IFlightSchedulePlaneService iFlightSchedulePlaneService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting DataInitializer...");

        // Kiểm tra xem đã có data chưa - nếu có thì KHÔNG chạy lại
        if (!airportService.getAllAirports().isEmpty()) {
            System.out.println("ℹ️ Data already exists — skipping initialization.");
            return;
        }

        // Nếu chưa có data -> Chạy toàn bộ initialization
        try {
            initializeUsers();
            List<Airport> airports = initializeAirports();
            List<FlightSchedule> seats = initializeSeats();
            List<Plane> flights = initializeFlights(airports);
            mapFlightsToSeats(flights, seats, airports);

            System.out.println("✅ Full data initialization completed successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error during data initialization: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 1. Khởi tạo Users (Admin + Demo User)
     */
    private void initializeUsers() {
        if (!iUsersService.getAllUsers().isEmpty()) {
            System.out.println("ℹ️ Users already exist — skipping user initialization.");
            return;
        }

        Users admin = new Users();
        admin.setFullName("System Administrator");
        admin.setEmail("admin@skyticket.com");
        admin.setPassword("123123");
        admin.setPhone("0123456789");
        admin.setPassportNumber("VN0000001");
        admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
        admin.setRoleUser(RoleUsers.ADMIN);

        Users demoUser = new Users();
        demoUser.setFullName("Demo User");
        demoUser.setEmail("user@skyticket.com");
        demoUser.setPassword("123123");
        demoUser.setPhone("0987654321");
        demoUser.setPassportNumber("VN0000002");
        demoUser.setDateOfBirth(LocalDate.of(1995, 5, 15));
        demoUser.setRoleUser(RoleUsers.USER);

        iUsersService.createUser(admin);
        iUsersService.createUser(demoUser);

        System.out.println("✅ Users initialized: 2 users created");
    }

    /**
     * 2. Khởi tạo 12 sân bay tại Việt Nam
     */
    private List<Airport> initializeAirports() {
        System.out.println("🛫 Initializing airports...");

        List<Airport> airports = List.of(
                createAirport("Sân bay Tân Sơn Nhất", "SGN", "TP. Hồ Chí Minh"),
                createAirport("Sân bay Nội Bài", "HAN", "Hà Nội"),
                createAirport("Sân bay Đà Nẵng", "DAD", "Đà Nẵng"),
                createAirport("Sân bay Cam Ranh", "CXR", "Khánh Hòa"),
                createAirport("Sân bay Phú Quốc", "PQC", "Kiên Giang"),
                createAirport("Sân bay Vân Đồn", "VDO", "Quảng Ninh"),
                createAirport("Sân bay Cát Bi", "HPH", "Hải Phòng"),
                createAirport("Sân bay Vinh", "VII", "Nghệ An"),
                createAirport("Sân bay Phú Bài", "HUI", "Thừa Thiên Huế"),
                createAirport("Sân bay Cần Thơ", "VCA", "Cần Thơ"),
                createAirport("Sân bay Liên Khương", "DLI", "Lâm Đồng"),
                createAirport("Sân bay Buôn Ma Thuột", "BMV", "Đắk Lắk")
        );

        System.out.println("✅ Airports initialized: " + airports.size() + " airports created");
        return airports;
    }

    /**
     * 3. Khởi tạo 10 loại ghế (5 Economy + 5 Business)
     */
    private List<FlightSchedule> initializeSeats() {
        System.out.println("💺 Initializing flight seats...");

        List<FlightSchedule> seats = List.of(
                // 5 ghế Economy
                createSeat(1, "Economy", 1500000),
                createSeat(2, "Economy", 1550000),
                createSeat(3, "Economy", 1600000),
                createSeat(4, "Economy", 1650000),
                createSeat(5, "Economy", 1700000),
                // 5 ghế Business
                createSeat(10, "Business", 3000000),
                createSeat(11, "Business", 3100000),
                createSeat(12, "Business", 3200000),
                createSeat(13, "Business", 3300000),
                createSeat(14, "Business", 3400000)
        );

        System.out.println("✅ Seats initialized: " + seats.size() + " seat types created");
        return seats;
    }

    /**
     * 4. Khởi tạo 12 chuyến bay
     */
    private List<Plane> initializeFlights(List<Airport> airports) {
        System.out.println("✈️ Initializing flights...");

        Airport tsn = airports.get(0);  // SGN
        Airport nba = airports.get(1);  // HAN
        Airport dad = airports.get(2);  // DAD
        Airport pqc = airports.get(4);  // PQC
        Airport vdo = airports.get(5);  // VDO
        Airport hph = airports.get(6);  // HPH
        Airport vii = airports.get(7);  // VII
        Airport vca = airports.get(9);  // VCA
        Airport dli = airports.get(10); // DLI
        Airport cxr = airports.get(3);  // CXR

        List<Plane> flights = List.of(
                createFlight("Vietnam Airlines", tsn, 8, 0, 125),
                createFlight("VietJet Air", nba, 10, 30, 130),
                createFlight("Bamboo Airways", tsn, 14, 0, 120),
                createFlight("Pacific Airlines", nba, 16, 30, 90),
                createFlight("Vietravel Airlines", dad, 18, 0, 100),
                createFlight("Vietnam Airlines", nba, 20, 0, 135),
                createFlight("VietJet Air", tsn, 5, 0, 75),
                createFlight("Vietnam Airlines", nba, 7, 0, 110),
                createFlight("Bamboo Airways", vdo, 9, 30, 150),
                createFlight("Pacific Airlines", dad, 12, 45, 95),
                createFlight("Vietravel Airlines", tsn, 15, 15, 60),
                createFlight("VietJet Air", vii, 18, 0, 105)
        );

        System.out.println("✅ Flights initialized: " + flights.size() + " flights created");
        return flights;
    }

    /**
     * 5. Map Flights với Seats (tạo FlightSchedule_Plane)
     */
    private void mapFlightsToSeats(List<Plane> flights, List<FlightSchedule> seats, List<Airport> airports) {
        System.out.println("🔗 Mapping flights to seats...");

        // Lấy các ghế
        FlightSchedule econSeat1 = seats.get(0);
        FlightSchedule econSeat2 = seats.get(1);
        FlightSchedule econSeat3 = seats.get(2);
        FlightSchedule econSeat4 = seats.get(3);
        FlightSchedule econSeat5 = seats.get(4);
        FlightSchedule bizSeat1 = seats.get(5);
        FlightSchedule bizSeat2 = seats.get(6);
        FlightSchedule bizSeat3 = seats.get(7);
        FlightSchedule bizSeat4 = seats.get(8);
        FlightSchedule bizSeat5 = seats.get(9);

        // Lấy các chuyến bay
        Plane flight1 = flights.get(0);  // VNA SGN->HAN
        Plane flight2 = flights.get(1);  // VJ HAN->SGN
        Plane flight3 = flights.get(2);  // BB SGN->HAN
        Plane flight4 = flights.get(3);  // PA HAN->DAD
        Plane flight5 = flights.get(4);  // VT DAD->SGN
        Plane flight6 = flights.get(5);  // VNA HAN->SGN
        Plane flight7 = flights.get(6);  // VJ SGN->PQC
        Plane flight8 = flights.get(7);  // VNA HAN->CXR
        Plane flight9 = flights.get(8);  // BB VDO->SGN
        Plane flight10 = flights.get(9); // PA DAD->HPH
        Plane flight11 = flights.get(10);// VT SGN->VCA
        Plane flight12 = flights.get(11);// VJ VII->DLI

        int mappingCount = 0;

        // Map chuyến bay 1: VNA SGN->HAN (đầy đủ 10 ghế)
        mappingCount += createFlightLinks(flight1, List.of(econSeat1, econSeat2, econSeat3, econSeat4, econSeat5,
                        bizSeat1, bizSeat2, bizSeat3, bizSeat4, bizSeat5),
                "SGN", "HAN");

        // Map chuyến bay 2: VJ HAN->SGN (5 Economy)
        mappingCount += createFlightLinks(flight2, List.of(econSeat1, econSeat2, econSeat3, econSeat4, econSeat5),
                "HAN", "SGN");

        // Map chuyến bay 3: BB SGN->HAN (3 Economy)
        mappingCount += createFlightLinks(flight3, List.of(econSeat1, econSeat2, econSeat3),
                "SGN", "HAN");

        // Map chuyến bay 4: PA HAN->DAD (2 Business)
        mappingCount += createFlightLinks(flight4, List.of(bizSeat1, bizSeat2),
                "HAN", "DAD");

        // Map chuyến bay 5: VT DAD->SGN (2 Economy)
        mappingCount += createFlightLinks(flight5, List.of(econSeat1, econSeat2),
                "DAD", "SGN");

        // Map chuyến bay 6: VNA HAN->SGN (5 Business)
        mappingCount += createFlightLinks(flight6, List.of(bizSeat1, bizSeat2, bizSeat3, bizSeat4, bizSeat5),
                "HAN", "SGN");

        // Map chuyến bay 7: VJ SGN->PQC (2 Economy)
        mappingCount += createFlightLinks(flight7, List.of(econSeat1, econSeat2),
                "SGN", "PQC");

        // Map chuyến bay 8: VNA HAN->CXR (2 Business)
        mappingCount += createFlightLinks(flight8, List.of(bizSeat1, bizSeat2),
                "HAN", "CXR");

        // Map chuyến bay 9: BB VDO->SGN (2 Economy)
        mappingCount += createFlightLinks(flight9, List.of(econSeat1, econSeat3),
                "VDO", "SGN");

        // Map chuyến bay 10: PA DAD->HPH (1 Economy)
        mappingCount += createFlightLinks(flight10, List.of(econSeat2),
                "DAD", "HPH");

        // Map chuyến bay 11: VT SGN->VCA (2 Business)
        mappingCount += createFlightLinks(flight11, List.of(bizSeat3, bizSeat4),
                "SGN", "VCA");

        // Map chuyến bay 12: VJ VII->DLI (2 Economy)
        mappingCount += createFlightLinks(flight12, List.of(econSeat4, econSeat5),
                "VII", "DLI");

        System.out.println("✅ Flight-Seat mappings created: " + mappingCount + " links");
    }

    // ==================== HELPER METHODS ====================

    private Airport createAirport(String name, String code, String location) {
        Airport airport = new Airport();
        airport.setAirportName(name);
        airport.setCode(code);
        airport.setLocation(location);
        return airportService.addAirport(airport);
    }

    private FlightSchedule createSeat(int seatNumber, String seatClass, int price) {
        FlightSchedule seat = new FlightSchedule();
        seat.setSeatNumber(seatNumber);
        seat.setSeatClass(seatClass);
        seat.setPrice(price);
        seat.setStatus("Available");
        return flightScheduleService.addFlightSchedulte(seat);
    }

    private Plane createFlight(String model, Airport departureAirport, int hour, int minute, int duration) {
        Plane plane = new Plane();
        plane.setPlaneModel(model);
        plane.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(hour).withMinute(minute));
        plane.setDuration(duration);
        plane.setStatus("Scheduled");
        plane.setAirport(departureAirport);
        return flightService.addFlight(plane);
    }

    private int createFlightLinks(Plane plane, List<FlightSchedule> seats, String takeOff, String land) {
        for (FlightSchedule seat : seats) {
            FlightSchedule_Plane link = new FlightSchedule_Plane();
            link.setPlane(plane);
            link.setFlightSchedule(seat);
            link.setTakeOff(takeOff);
            link.setLand(land);
            link.setTakeOffTime(plane.getDepartureTime());
            link.setLandTime(plane.getDepartureTime().plusMinutes(plane.getDuration()));
            iFlightSchedulePlaneService.add(link);
        }
        return seats.size();
    }
}
