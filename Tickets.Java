/**
 * Abstract parent class representing an individual movie ticket.
 * Demonstrates inheritance and polymorphism through subclasses AdultTicket,
 * ChildTicket, and SeniorTicket, which override calculatePrice().
 */
public abstract class Ticket {
    protected int ticketID;
    protected String seatNumber;
    protected double ticketPrice;
    protected String ticketType;
    protected Showtime showtime;

    /**
     * Constructs a Ticket instance.
     *
     * @param ticketID    Unique identifier for the ticket
     * @param seatNumber  Assigned seat (e.g., "A1")
     * @param ticketPrice Base price of the ticket
     * @param ticketType  Category name (e.g., "Adult", "Child", "Senior")
     * @param showtime    Associated screening
     */
    public Ticket(int ticketID, String seatNumber, double ticketPrice, String ticketType, Showtime showtime) {
        if (ticketID <= 0) {
            throw new IllegalArgumentException("Ticket ID must be a positive integer.");
        }
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be blank.");
        }
        if (ticketPrice < 0) {
            throw new IllegalArgumentException("Ticket price cannot be negative.");
        }
        if (ticketType == null || ticketType.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket type cannot be blank.");
        }
        if (showtime == null) {
            throw new IllegalArgumentException("Showtime cannot be null.");
        }

        this.ticketID = ticketID;
        this.seatNumber = seatNumber.trim().toUpperCase();
        this.ticketPrice = ticketPrice;
        this.ticketType = ticketType.trim();
        this.showtime = showtime;
    }

    /**
     * Polymorphic method to calculate ticket price.
     * Overridden by each subclass to apply category-specific pricing.
     *
     * @return final calculated ticket price
     */
    public abstract double calculatePrice();

    // Getters
    public int getTicketID() {
        return ticketID;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public String getTicketType() {
        return ticketType;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    /**
     * Formats individual ticket details.
     */
    public String getTicketDetails() {
        return String.format("Ticket #%-4d | Type: %-7s | Seat: %-3s | Price: $%.2f",
            ticketID, ticketType, seatNumber, calculatePrice());
    }

    @Override
    public String toString() {
        return getTicketDetails();
    }
}
