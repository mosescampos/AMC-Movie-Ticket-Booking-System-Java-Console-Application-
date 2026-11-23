import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Represents an individual screening of a movie.
 * Maintains its own reserved seat collection using HashSet so that seats
 * are tracked independently per showtime.
 */
public class Showtime {
    private int showtimeID;
    private Movie movie;
    private LocalDate showDate;
    private LocalTime showTime;
    private HashSet<String> reservedSeats;

    // Theater auditorium layout: 5 Rows (A-E) x 8 Seats (1-8) = 40 Total Seats
    public static final int TOTAL_ROWS = 5;
    public static final int SEATS_PER_ROW = 8;
    public static final int TOTAL_SEATS = TOTAL_ROWS * SEATS_PER_ROW;
    private static final Pattern SEAT_PATTERN = Pattern.compile("^[A-E][1-8]$");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    /**
     * Constructs a Showtime instance.
     *
     * @param showtimeID Unique screening identifier
     * @param movie      Associated movie object
     * @param showDate   Screening date
     * @param showTime   Screening start time
     */
    public Showtime(int showtimeID, Movie movie, LocalDate showDate, LocalTime showTime) {
        if (showtimeID <= 0) {
            throw new IllegalArgumentException("Showtime ID must be a positive integer.");
        }
        if (movie == null) {
            throw new IllegalArgumentException("Associated movie cannot be null.");
        }
        if (showDate == null) {
            throw new IllegalArgumentException("Show date cannot be null.");
        }
        if (showTime == null) {
            throw new IllegalArgumentException("Show time cannot be null.");
        }

        this.showtimeID = showtimeID;
        this.movie = movie;
        this.showDate = showDate;
        this.showTime = showTime;
        this.reservedSeats = new HashSet<>();
    }

    // Getters
    public int getShowtimeID() {
        return showtimeID;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public HashSet<String> getReservedSeats() {
        return reservedSeats;
    }

    public int getTotalSeats() {
        return TOTAL_SEATS;
    }

    public int getAvailableSeatCount() {
        return TOTAL_SEATS - reservedSeats.size();
    }

    /**
     * Validates whether a seat format is valid (A1 through E8).
     */
    public boolean isValidSeat(String seatNumber) {
        if (seatNumber == null) return false;
        return SEAT_PATTERN.matcher(seatNumber.trim().toUpperCase()).matches();
    }

    /**
     * Checks if a seat is valid and currently available (not reserved).
     */
    public boolean checkSeatAvailability(String seatNumber) {
        if (!isValidSeat(seatNumber)) {
            return false;
        }
        String normalizedSeat = seatNumber.trim().toUpperCase();
        return !reservedSeats.contains(normalizedSeat);
    }

    /**
     * Reserves a seat for this screening.
     *
     * @param seatNumber Seat label (e.g., "A1")
     * @return true if seat was successfully reserved
     * @throws IllegalArgumentException if seat number format is invalid
     * @throws IllegalStateException    if seat is already reserved
     */
    public boolean reserveSeat(String seatNumber) {
        if (!isValidSeat(seatNumber)) {
            throw new IllegalArgumentException("Invalid seat: '" + seatNumber + "'. Valid seats are A1 through E8.");
        }
        String normalized = seatNumber.trim().toUpperCase();
        if (reservedSeats.contains(normalized)) {
            throw new IllegalStateException("Seat '" + normalized + "' is already reserved for this showtime.");
        }
        return reservedSeats.add(normalized);
    }

    /**
     * Releases a reserved seat, making it available again.
     *
     * @param seatNumber Seat label to release
     * @return true if seat was successfully released
     */
    public boolean releaseSeat(String seatNumber) {
        if (seatNumber == null) return false;
        return reservedSeats.remove(seatNumber.trim().toUpperCase());
    }

    /**
     * Formats detailed showtime information.
     */
    public String getShowtimeDetails() {
        return String.format(
            "Showtime ID : #%d%n" +
            "Movie       : %s (%s)%n" +
            "Date        : %s%n" +
            "Time        : %s%n" +
            "Seats Avail : %d / %d",
            showtimeID,
            movie.getTitle(),
            movie.getRating(),
            showDate.format(DATE_FORMATTER),
            showTime.format(TIME_FORMATTER),
            getAvailableSeatCount(),
            TOTAL_SEATS
        );
    }

    /**
     * Renders a visual ASCII seat map of the theater auditorium.
     */
    public void displaySeatMap() {
        System.out.println();
        System.out.println("  ========================== SCREEN ==========================");
        System.out.println("          [1]    [2]    [3]    [4]       [5]    [6]    [7]    [8]");
        System.out.println("  ------------------------------------------------------------");

        char[] rows = {'A', 'B', 'C', 'D', 'E'};
        for (char row : rows) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("  Row %c:  ", row));
            for (int col = 1; col <= SEATS_PER_ROW; col++) {
                String seat = "" + row + col;
                if (reservedSeats.contains(seat)) {
                    sb.append("[ X ]  ");
                } else {
                    sb.append(String.format("[%s]  ", seat));
                }
                if (col == 4) {
                    sb.append("   "); // Aisle gap between seats 4 and 5
                }
            }
            System.out.println(sb.toString());
        }
        System.out.println("  ------------------------------------------------------------");
        System.out.printf("  Legend: [Seat] = Available   [ X ] = Reserved   (Available: %d / %d)%n",
            getAvailableSeatCount(), TOTAL_SEATS);
        System.out.println();
    }

    @Override
    public String toString() {
        return String.format("[Showtime #%d] %s | %s @ %s | Seats: %d/%d available",
            showtimeID,
            movie.getTitle(),
            showDate.format(DATE_FORMATTER),
            showTime.format(TIME_FORMATTER),
            getAvailableSeatCount(),
            TOTAL_SEATS
        );
    }
}
