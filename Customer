import java.util.ArrayList;

/**
 * Stores customer information and their booking history.
 */
public class Customer {
    private int customerID;
    private String customerName;
    private String email;
    private ArrayList<Booking> bookingHistory;

    /**
     * Constructs a Customer with validation.
     *
     * @param customerID   Unique customer ID
     * @param customerName Customer name
     * @param email        Customer email
     */
    public Customer(int customerID, String customerName, String email) {
        if (customerID <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive integer.");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be blank.");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid customer email required.");
        }

        this.customerID = customerID;
        this.customerName = customerName.trim();
        this.email = email.trim();
        this.bookingHistory = new ArrayList<>();
    }

    // Getters
    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Booking> getBookingHistory() {
        return bookingHistory;
    }

    /**
     * Records a new booking in the customer's history.
     */
    public void makeBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }
        bookingHistory.add(booking);
    }

    /**
     * Displays all bookings made by this customer.
     */
    public void viewBookings() {
        System.out.println("==============================================================");
        System.out.printf("  BOOKING HISTORY FOR: %s (ID: %d)%n", customerName, customerID);
        System.out.println("==============================================================");
        if (bookingHistory.isEmpty()) {
            System.out.println("  No booking history found.");
        } else {
            for (Booking b : bookingHistory) {
                System.out.println("  " + b.toString());
            }
        }
        System.out.println("==============================================================");
    }

    /**
     * Cancels an eligible booking by booking ID.
     *
     * @param bookingID ID of reservation to cancel
     * @return true if found and cancelled, false otherwise
     */
    public boolean cancelBooking(int bookingID) {
        for (Booking b : bookingHistory) {
            if (b.getBookingID() == bookingID) {
                if ("CANCELLED".equalsIgnoreCase(b.getBookingStatus())) {
                    System.out.println("  Booking #" + bookingID + " is already cancelled.");
                    return false;
                }
                b.cancelBooking();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns formatted customer details.
     */
    public String getCustomerDetails() {
        return String.format("Customer #%d: %s (%s) | Total Bookings: %d",
            customerID, customerName, email, bookingHistory.size());
    }

    @Override
    public String toString() {
        return getCustomerDetails();
    }
}
