import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Standalone automated test runner for verifying all CST250 requirements,
 * edge cases, polymorphism, algorithms, and validation checks.
 */
public class SystemTest {
    public static void main(String[] args) {
        System.out.println("Running comprehensive AMC Booking System Unit & Integration Tests...\n");
        int passed = 0;
        int total = 0;

        // Test 1: Bubble Sort on Empty and Single List
        total++;
        MovieManager mmEmpty = new MovieManager();
        mmEmpty.bubbleSortMoviesByTitle();
        mmEmpty.addMovie(new Movie(1, "Single Movie", "Action", "PG", LocalDate.of(2026, 12, 1)));
        mmEmpty.bubbleSortMoviesByTitle();
        if (mmEmpty.getMovies().size() == 1) {
            System.out.println("[PASS] Test 1: Bubble Sort handles empty and single-element lists.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 1: Bubble Sort failed on empty/single list.");
        }

        // Test 2: Bubble Sort Alphabetical Ordering with Multiple Elements
        total++;
        MovieManager mm = new MovieManager();
        mm.addMovie(new Movie(101, "Zootopia 2", "Animation", "PG", LocalDate.of(2026, 12, 1)));
        mm.addMovie(new Movie(102, "Avengers: Doomsday", "Action", "PG-13", LocalDate.of(2026, 12, 18)));
        mm.addMovie(new Movie(103, "Dune: Part Three", "Sci-Fi", "PG-13", LocalDate.of(2026, 12, 18)));
        mm.bubbleSortMoviesByTitle();
        if (mm.getMovies().get(0).getTitle().equals("Avengers: Doomsday") &&
            mm.getMovies().get(1).getTitle().equals("Dune: Part Three") &&
            mm.getMovies().get(2).getTitle().equals("Zootopia 2")) {
            System.out.println("[PASS] Test 2: Bubble Sort correctly arranges elements alphabetically.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 2: Bubble Sort failed ordering.");
        }

        // Test 3: Search by Title and Genre (case-insensitive substring)
        total++;
        ArrayList<Movie> titleMatches = mm.searchMoviesByTitle("doomsday");
        ArrayList<Movie> genreMatches = mm.searchMoviesByGenre("sci-fi");
        if (titleMatches.size() == 1 && titleMatches.get(0).getMovieID() == 102 &&
            genreMatches.size() == 1 && genreMatches.get(0).getMovieID() == 103) {
            System.out.println("[PASS] Test 3: Title and genre case-insensitive substring search.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 3: Search failed.");
        }

        // Test 4: Duplicate Movie ID Rejection
        total++;
        boolean dupAdded = mm.addMovie(new Movie(101, "Another Movie", "Drama", "R", LocalDate.of(2026, 12, 10)));
        if (!dupAdded && mm.getMovies().size() == 3) {
            System.out.println("[PASS] Test 4: Duplicate Movie ID rejected via HashMap.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 4: Duplicate Movie ID was not rejected.");
        }

        // Test 5: Seat Format Validation (A1-E8)
        total++;
        Showtime st = new Showtime(201, mm.findMovieByID(102), LocalDate.of(2026, 12, 18), LocalTime.of(19, 0));
        if (st.isValidSeat("A1") && st.isValidSeat("E8") && !st.isValidSeat("A9") && !st.isValidSeat("F1") && !st.isValidSeat("ZZ")) {
            System.out.println("[PASS] Test 5: Seat boundaries and regex format validation.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 5: Seat format validation failed.");
        }

        // Test 6: Duplicate Seat Reservation (HashSet)
        total++;
        st.reserveSeat("B4");
        boolean canReserveAgain = st.checkSeatAvailability("B4");
        if (!canReserveAgain) {
            System.out.println("[PASS] Test 6: Occupied seat reservation prevented via HashSet.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 6: Seat B4 allowed duplicate reservation.");
        }

        // Test 7: Polymorphic Ticket Pricing (Adult, Child, Senior)
        total++;
        Ticket tAdult = new AdultTicket(1, "A1", st);
        Ticket tChild = new ChildTicket(2, "A2", st);
        Ticket tSenior = new SeniorTicket(3, "A3", st);
        if (tAdult.calculatePrice() == 16.50 && tChild.calculatePrice() == 12.00 && tSenior.calculatePrice() == 13.50) {
            System.out.println("[PASS] Test 7: Polymorphic pricing across Adult ($16.50), Child ($12.00), Senior ($13.50).");
            passed++;
        } else {
            System.out.println("[FAIL] Test 7: Polymorphic pricing mismatch.");
        }

        // Test 8: Booking Manager Atomic Transaction
        total++;
        BookingManager bm = new BookingManager();
        bm.addShowtime(st);
        Customer cust = new Customer(1, "Test User", "test@test.com");
        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets.add(new AdultTicket(bm.generateTicketID(), "C1", st));
        tickets.add(new ChildTicket(bm.generateTicketID(), "C2", st));
        Booking booking = bm.createBooking(cust, st, tickets);
        if (booking != null && booking.getTotalPrice() == (16.50 + 12.00) && !st.checkSeatAvailability("C1") && !st.checkSeatAvailability("C2")) {
            System.out.println("[PASS] Test 8: Atomic booking creation updates seat availability and total price.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 8: Booking creation failed.");
        }

        // Test 9: Cancellation Releases Seats
        total++;
        bm.cancelBooking(booking.getBookingID());
        if ("CANCELLED".equals(booking.getBookingStatus()) && st.checkSeatAvailability("C1") && st.checkSeatAvailability("C2")) {
            System.out.println("[PASS] Test 9: Booking cancellation releases reserved seats back to showtime.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 9: Cancellation seat release failed.");
        }

        // Test 10: Queue FIFO Behavior
        total++;
        Booking bQueue = new Booking(9999, cust);
        bm.enqueuePendingBooking(bQueue);
        Booking peeked = bm.peekNextPendingBooking();
        Booking polled = bm.processNextBooking();
        if (peeked == bQueue && polled == bQueue && bm.getPendingQueueSize() == 0) {
            System.out.println("[PASS] Test 10: ArrayDeque FIFO queue correctly executes offer(), peek(), and poll().");
            passed++;
        } else {
            System.out.println("[FAIL] Test 10: Queue FIFO failed.");
        }

        // Test 11: Showtime Deletion Protection
        total++;
        Showtime stProtected = new Showtime(301, mm.findMovieByID(102), LocalDate.of(2026, 12, 18), LocalTime.of(21, 0));
        bm.addShowtime(stProtected);
        ArrayList<Ticket> activeTickets = new ArrayList<>();
        activeTickets.add(new AdultTicket(bm.generateTicketID(), "A5", stProtected));
        bm.createBooking(cust, stProtected, activeTickets);
        boolean exceptionThrown = false;
        try {
            bm.removeShowtime(301);
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }
        if (exceptionThrown) {
            System.out.println("[PASS] Test 11: Showtime deletion blocked when active bookings exist.");
            passed++;
        } else {
            System.out.println("[FAIL] Test 11: Showtime deletion was not blocked.");
        }

        System.out.printf("%nSummary: %d / %d Tests Passed Successfully!%n", passed, total);
    }
}
