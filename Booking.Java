import java.util.ArrayList;

/**
 * Represents a customer's movie ticket booking reservation.
 * Stores purchased tickets in an ArrayList and calculates the total price
 * using polymorphic dispatch on each ticket's calculatePrice() method.
 */
public class Booking {
    private int bookingID;
    private Customer customer;
    private ArrayList<Ticket> tickets;
    private double totalPrice;
    private String bookingStatus; // "CONFIRMED", "CANCELLED", "PENDING"

    /**
     * Constructs a Booking for a specified customer.
     *
     * @param bookingID Unique reservation identifier
     * @param customer  The customer making the reservation
     */
    public Booking(int bookingID, Customer customer) {
        if (bookingID <= 0) {
            throw new IllegalArgumentException("Booking ID must be a positive integer.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        this.bookingID = bookingID;
        this.customer = customer;
        this.tickets = new ArrayList<>();
        this.totalPrice = 0.0;
        this.bookingStatus = "CONFIRMED";
    }

    /**
     * Adds an individual ticket to the booking and updates the total price.
     *
     * @param ticket Ticket to attach to this reservation
     */
    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null.");
        }
        tickets.add(ticket);
        calculateTotal();
    }

    /**
     * Calculates total price using polymorphism across all tickets in this booking.
     *
     * @return cumulative total price
     */
    public double calculateTotal() {
        double sum = 0.0;
        for (Ticket ticket : tickets) {
            sum += ticket.calculatePrice(); // Polymorphic dispatch
        }
        this.totalPrice = sum;
        return sum;
    }

    /**
     * Cancels the booking and updates status.
     */
    public void cancelBooking() {
        this.bookingStatus = "CANCELLED";
    }

    // Getters and Setters
    public int getBookingID() {
        return bookingID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be blank.");
        }
        this.bookingStatus = status.trim().toUpperCase();
    }

    /**
     * Displays a formatted AMC Theatres booking confirmation receipt.
     */
    public void displayConfirmation() {
        System.out.println("==============================================================");
        System.out.println("              AMC THEATRES - BOOKING CONFIRMATION             ");
        System.out.println("==============================================================");
        System.out.printf("  Booking Confirmation #: AMC-%06d%n", bookingID);
        System.out.printf("  Status                 : [%s]%n", bookingStatus);
        System.out.printf("  Guest Name             : %s%n", customer.getCustomerName());
        System.out.printf("  Guest Email            : %s%n", customer.getEmail());

        if (!tickets.isEmpty()) {
            Showtime showtime = tickets.get(0).getShowtime();
            System.out.printf("  Movie Title            : %s (%s)%n",
                showtime.getMovie().getTitle(), showtime.getMovie().getRating());
            System.out.printf("  Date & Time            : %s | %s%n",
                showtime.getShowDate(), showtime.getShowTime());
            System.out.println("  ------------------------------------------------------------");
            System.out.println("  Purchased Tickets:");
            for (Ticket t : tickets) {
                System.out.printf("    * Seat %-3s (%-7s Ticket) : $%.2f%n",
                    t.getSeatNumber(), t.getTicketType(), t.calculatePrice());
            }
        }
        System.out.println("  ------------------------------------------------------------");
        System.out.printf("  TOTAL PAID             : $%.2f%n", totalPrice);
        System.out.println("==============================================================");
        System.out.println("  Present this confirmation receipt at the theater entrance.   ");
        System.out.println("==============================================================");
    }

    @Override
    public String toString() {
        String movieTitle = (!tickets.isEmpty()) ? tickets.get(0).getShowtime().getMovie().getTitle() : "N/A";
        return String.format("[Booking #%04d] %s | %d Tickets | Total: $%.2f | Status: %s",
            bookingID, movieTitle, tickets.size(), totalPrice, bookingStatus);
    }
}
