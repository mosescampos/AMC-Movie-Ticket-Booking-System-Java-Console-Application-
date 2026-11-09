/**
 * Represents a discounted child movie ticket (ages 2-12).
 * Extends Ticket and overrides calculatePrice() to apply the child admission rate ($12.00).
 */
public class ChildTicket extends Ticket {
    public static final double CHILD_BASE_PRICE = 12.00;

    /**
     * Constructs a ChildTicket.
     *
     * @param ticketID   Unique ticket identifier
     * @param seatNumber Assigned seat (e.g., "A1")
     * @param showtime   Associated screening
     */
    public ChildTicket(int ticketID, String seatNumber, Showtime showtime) {
        super(ticketID, seatNumber, CHILD_BASE_PRICE, "Child", showtime);
    }

    @Override
    public double calculatePrice() {
        return CHILD_BASE_PRICE;
    }
}
