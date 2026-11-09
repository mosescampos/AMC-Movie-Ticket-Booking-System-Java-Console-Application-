/**
 * Represents a discounted senior movie ticket (ages 60+).
 * Extends Ticket and overrides calculatePrice() to apply the senior admission rate ($13.50).
 */
public class SeniorTicket extends Ticket {
    public static final double SENIOR_BASE_PRICE = 13.50;

    /**
     * Constructs a SeniorTicket.
     *
     * @param ticketID   Unique ticket identifier
     * @param seatNumber Assigned seat (e.g., "A1")
     * @param showtime   Associated screening
     */
    public SeniorTicket(int ticketID, String seatNumber, Showtime showtime) {
        super(ticketID, seatNumber, SENIOR_BASE_PRICE, "Senior", showtime);
    }

    @Override
    public double calculatePrice() {
        return SENIOR_BASE_PRICE;
    }
}
