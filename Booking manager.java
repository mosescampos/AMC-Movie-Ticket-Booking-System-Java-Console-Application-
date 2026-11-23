import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Manages showtimes, seat reservations, pending booking FIFO queue,
 * and completed/cancelled booking ledgers.
 */
public class BookingManager {
    private ArrayList<Showtime> showtimes;
    private Queue<Booking> pendingBookings;
    private HashMap<Integer, Booking> completedBookings;
    private int nextBookingID;
    private int nextTicketID;

    /**
     * Initializes BookingManager with empty collections and starting ID counters.
     */
    public BookingManager() {
        this.showtimes = new ArrayList<>();
        this.pendingBookings = new ArrayDeque<>();
        this.completedBookings = new HashMap<>();
        this.nextBookingID = 1001;
        this.nextTicketID = 5001;
    }

    // ID Generators
    public int generateBookingID() {
        return nextBookingID++;
    }

    public int generateTicketID() {
        return nextTicketID++;
    }

    /**
     * Registers a new showtime in the system.
     *
     * @param showtime Showtime to add
     */
    public void addShowtime(Showtime showtime) {
        if (showtime == null) {
            throw new IllegalArgumentException("Showtime cannot be null.");
        }
        showtimes.add(showtime);
    }

    /**
     * Finds a showtime by its unique ID.
     *
     * @param showtimeID ID to find
     * @return Showtime object or null if not found
     */
    public Showtime findShowtime(int showtimeID) {
        for (Showtime s : showtimes) {
            if (s.getShowtimeID() == showtimeID) {
                return s;
            }
        }
        return null;
    }

    /**
     * Removes a showtime only if no active (CONFIRMED) customer reservations exist.
     *
     * @param showtimeID Showtime to remove
     * @return true if removed
     * @throws IllegalStateException if active reservations are linked to this showtime
     */
    public boolean removeShowtime(int showtimeID) {
        Showtime target = findShowtime(showtimeID);
        if (target == null) {
            return false;
        }

        // Integrity check: verify no active bookings exist for this showtime
        for (Booking b : completedBookings.values()) {
            if ("CONFIRMED".equalsIgnoreCase(b.getBookingStatus())) {
                for (Ticket t : b.getTickets()) {
                    if (t.getShowtime().getShowtimeID() == showtimeID) {
                        throw new IllegalStateException(
                            "Cannot remove Showtime #" + showtimeID +
                            " because active reservation Booking #" + b.getBookingID() + " is linked to it."
                        );
                    }
                }
            }
        }

        return showtimes.remove(target);
    }

    /**
     * Atomically validates all selected seats and completes a ticket booking.
     * If ANY seat is unavailable or invalid, the entire booking is rejected
     * without locking any seats.
     *
     * @param customer Customer making reservation
     * @param showtime Target screening
     * @param tickets  List of created ticket objects
     * @return Completed Booking object
     * @throws IllegalStateException if any seat is already reserved
     * @throws IllegalArgumentException if invalid parameters
     */
    public Booking createBooking(Customer customer, Showtime showtime, ArrayList<Ticket> tickets) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (showtime == null) {
            throw new IllegalArgumentException("Showtime cannot be null.");
        }
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("Booking must contain at least one ticket.");
        }

        // STEP 1: All-or-nothing validation pass
        for (Ticket ticket : tickets) {
            String seat = ticket.getSeatNumber();
            if (!showtime.isValidSeat(seat)) {
                throw new IllegalArgumentException("Seat '" + seat + "' is not a valid auditorium seat (A1-E8).");
            }
            if (!showtime.checkSeatAvailability(seat)) {
                throw new IllegalStateException("Seat '" + seat + "' is already reserved for this showtime.");
            }
        }

        // STEP 2: Reserve each seat once validation succeeds
        for (Ticket ticket : tickets) {
            showtime.reserveSeat(ticket.getSeatNumber());
        }

        // STEP 3: Assemble Booking
        int bookingID = generateBookingID();
        Booking booking = new Booking(bookingID, customer);
        for (Ticket ticket : tickets) {
            booking.addTicket(ticket);
        }
        booking.setBookingStatus("CONFIRMED");

        // Record in manager ledger and customer history
        completedBookings.put(bookingID, booking);
        customer.makeBooking(booking);

        return booking;
    }

    /**
     * Adds a booking to the pending queue for FIFO processing.
     * Demonstrates Queue.offer().
     */
    public void enqueuePendingBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }
        booking.setBookingStatus("PENDING");
        pendingBookings.offer(booking);
    }

    /**
     * Inspects the next pending booking without removing it.
     * Demonstrates Queue.peek().
     */
    public Booking peekNextPendingBooking() {
        return pendingBookings.peek();
    }

    /**
     * Processes and confirms the oldest pending booking in FIFO order.
     * Demonstrates Queue.poll().
     *
     * @return processed Booking or null if queue is empty
     */
    public Booking processNextBooking() {
        if (pendingBookings.isEmpty()) {
            return null;
        }

        Booking booking = pendingBookings.poll();
        if (booking != null) {
            booking.setBookingStatus("CONFIRMED");
            completedBookings.put(booking.getBookingID(), booking);
            booking.getCustomer().makeBooking(booking);
        }
        return booking;
    }

    /**
     * Cancels an existing booking, releases reserved seats back to the showtime,
     * and updates customer booking status.
     *
     * @param bookingID Unique reservation ID
     * @return true if successfully cancelled
     * @throws IllegalArgumentException if booking does not exist
     * @throws IllegalStateException if booking is already cancelled
     */
    public boolean cancelBooking(int bookingID) {
        Booking booking = completedBookings.get(bookingID);
        if (booking == null) {
            throw new IllegalArgumentException("Booking ID #" + bookingID + " not found.");
        }
        if ("CANCELLED".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("Booking ID #" + bookingID + " is already cancelled.");
        }

        // Release all reserved seats associated with tickets in this booking
        for (Ticket ticket : booking.getTickets()) {
            ticket.getShowtime().releaseSeat(ticket.getSeatNumber());
        }

        // Update booking status
        booking.cancelBooking();
        return true;
    }

    /**
     * Retrieves all showtimes associated with a particular movie ID.
     */
    public ArrayList<Showtime> getShowtimesForMovie(int movieID) {
        ArrayList<Showtime> result = new ArrayList<>();
        for (Showtime s : showtimes) {
            if (s.getMovie().getMovieID() == movieID) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Displays all booking records (completed and cancelled) for employee review.
     */
    public void displayBookingHistory() {
        System.out.println("==========================================================================================");
        System.out.println("                            AMC MASTER BOOKING RECORDS                                     ");
        System.out.println("==========================================================================================");
        System.out.printf("%-10s %-18s %-28s %-7s %-10s %-12s%n",
            "BOOKING ID", "CUSTOMER", "MOVIE", "TICKETS", "TOTAL", "STATUS");
        System.out.println("------------------------------------------------------------------------------------------");
        if (completedBookings.isEmpty()) {
            System.out.println("No booking records recorded in the system.");
        } else {
            for (Booking b : completedBookings.values()) {
                String movie = (!b.getTickets().isEmpty()) ?
                    b.getTickets().get(0).getShowtime().getMovie().getTitle() : "N/A";
                System.out.printf("AMC-%-6d %-18s %-28s %-7d $%-9.2f [%s]%n",
                    b.getBookingID(),
                    truncate(b.getCustomer().getCustomerName(), 17),
                    truncate(movie, 26),
                    b.getTickets().size(),
                    b.getTotalPrice(),
                    b.getBookingStatus());
            }
        }
        System.out.println("==========================================================================================");
    }

    public ArrayList<Showtime> getAllShowtimes() {
        return showtimes;
    }

    public HashMap<Integer, Booking> getCompletedBookings() {
        return completedBookings;
    }

    public Queue<Booking> getPendingBookingsQueue() {
        return pendingBookings;
    }

    public int getPendingQueueSize() {
        return pendingBookings.size();
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 2) + "..";
    }
}
