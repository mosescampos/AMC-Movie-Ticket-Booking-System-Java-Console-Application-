import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Handles persistent file storage (Save and Load) for movie listings,
 * showtimes, reserved seats, customers, and bookings (Feature F12).
 */
public class DataManager {
    public static final String DEFAULT_DATA_FILE = "amc_system_data.txt";

    /**
     * Saves complete system state to a designated file.
     *
     * @param movieManager   Movie manager instance
     * @param bookingManager Booking manager instance
     * @param customers      List of registered customers
     * @param filePath       Target file path
     * @throws IOException if writing fails
     */
    public static void saveData(MovieManager movieManager, BookingManager bookingManager,
                                ArrayList<Customer> customers, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 1. Save Customers
            writer.write("# CUSTOMERS\n");
            for (Customer c : customers) {
                writer.write(String.format("CUSTOMER|%d|%s|%s%n",
                    c.getCustomerID(), c.getCustomerName(), c.getEmail()));
            }

            // 2. Save Movies
            writer.write("# MOVIES\n");
            for (Movie m : movieManager.getMovies()) {
                writer.write(String.format("MOVIE|%d|%s|%s|%s|%s%n",
                    m.getMovieID(), m.getTitle(), m.getGenre(), m.getRating(), m.getReleaseDate()));
            }

            // 3. Save Showtimes
            writer.write("# SHOWTIMES\n");
            for (Showtime s : bookingManager.getAllShowtimes()) {
                StringBuilder seatList = new StringBuilder();
                for (String seat : s.getReservedSeats()) {
                    if (seatList.length() > 0) seatList.append(",");
                    seatList.append(seat);
                }
                writer.write(String.format("SHOWTIME|%d|%d|%s|%s|%s%n",
                    s.getShowtimeID(),
                    s.getMovie().getMovieID(),
                    s.getShowDate(),
                    s.getShowTime(),
                    seatList.toString()));
            }

            // 4. Save Bookings and Tickets
            writer.write("# BOOKINGS\n");
            for (Booking b : bookingManager.getCompletedBookings().values()) {
                writer.write(String.format("BOOKING|%d|%d|%s|%.2f%n",
                    b.getBookingID(),
                    b.getCustomer().getCustomerID(),
                    b.getBookingStatus(),
                    b.getTotalPrice()));

                for (Ticket t : b.getTickets()) {
                    writer.write(String.format("TICKET|%d|%d|%s|%s|%d%n",
                        t.getTicketID(),
                        b.getBookingID(),
                        t.getSeatNumber(),
                        t.getTicketType(),
                        t.getShowtime().getShowtimeID()));
                }
            }
        }
    }

    /**
     * Loads system state from a designated file.
     *
     * @param movieManager   Movie manager to populate
     * @param bookingManager Booking manager to populate
     * @param customers      Customer list to populate
     * @param filePath       Source file path
     * @throws IOException if reading fails
     */
    public static void loadData(MovieManager movieManager, BookingManager bookingManager,
                                ArrayList<Customer> customers, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Data file '" + filePath + "' does not exist.");
        }

        // Clear existing in-memory state
        movieManager.getMovies().clear();
        bookingManager.getAllShowtimes().clear();
        bookingManager.getCompletedBookings().clear();
        customers.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Booking currentBooking = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\|");
                String recordType = parts[0];

                switch (recordType) {
                    case "CUSTOMER": {
                        int id = Integer.parseInt(parts[1]);
                        String name = parts[2];
                        String email = parts[3];
                        customers.add(new Customer(id, name, email));
                        break;
                    }
                    case "MOVIE": {
                        int id = Integer.parseInt(parts[1]);
                        String title = parts[2];
                        String genre = parts[3];
                        String rating = parts[4];
                        LocalDate releaseDate = LocalDate.parse(parts[5]);
                        movieManager.addMovie(new Movie(id, title, genre, rating, releaseDate));
                        break;
                    }
                    case "SHOWTIME": {
                        int showtimeID = Integer.parseInt(parts[1]);
                        int movieID = Integer.parseInt(parts[2]);
                        LocalDate date = LocalDate.parse(parts[3]);
                        LocalTime time = LocalTime.parse(parts[4]);

                        Movie movie = movieManager.findMovieByID(movieID);
                        if (movie != null) {
                            Showtime showtime = new Showtime(showtimeID, movie, date, time);
                            if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                                String[] seats = parts[5].split(",");
                                for (String seat : seats) {
                                    if (!seat.trim().isEmpty()) {
                                        showtime.reserveSeat(seat.trim());
                                    }
                                }
                            }
                            bookingManager.addShowtime(showtime);
                        }
                        break;
                    }
                    case "BOOKING": {
                        int bookingID = Integer.parseInt(parts[1]);
                        int customerID = Integer.parseInt(parts[2]);
                        String status = parts[3];

                        Customer customer = null;
                        for (Customer c : customers) {
                            if (c.getCustomerID() == customerID) {
                                customer = c;
                                break;
                            }
                        }

                        if (customer != null) {
                            currentBooking = new Booking(bookingID, customer);
                            currentBooking.setBookingStatus(status);
                            bookingManager.getCompletedBookings().put(bookingID, currentBooking);
                            customer.makeBooking(currentBooking);
                        }
                        break;
                    }
                    case "TICKET": {
                        if (currentBooking != null) {
                            int ticketID = Integer.parseInt(parts[1]);
                            String seatNumber = parts[3];
                            String type = parts[4];
                            int showtimeID = Integer.parseInt(parts[5]);

                            Showtime showtime = bookingManager.findShowtime(showtimeID);
                            if (showtime != null) {
                                Ticket ticket;
                                if ("Adult".equalsIgnoreCase(type)) {
                                    ticket = new AdultTicket(ticketID, seatNumber, showtime);
                                } else if ("Child".equalsIgnoreCase(type)) {
                                    ticket = new ChildTicket(ticketID, seatNumber, showtime);
                                } else {
                                    ticket = new SeniorTicket(ticketID, seatNumber, showtime);
                                }
                                currentBooking.addTicket(ticket);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}
