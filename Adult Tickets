/**
 * Represents a standard adult movie ticket.
 * Extends Ticket and overrides calculatePrice() to apply the adult admission rate ($16.50).
 */
public class AdultTicket extends Ticket {
    public static final double ADULT_BASE_PRICE = 16.50;

    /**
     * Constructs an AdultTicket.
     *
     * @param ticketID   Unique ticket identifier
     * @param seatNumber Assigned seat (e.g., "A1")
     * @param showtime   Associated screening
     */
    public AdultTicket(int ticketID, String seatNumber, Showtime showtime) {
        super(ticketID, seatNumber, ADULT_BASE_PRICE, "Adult", showtime);
    }

    @Override
    public double calculatePrice() {
        return ADULT_BASE_PRICE;
    }
}
