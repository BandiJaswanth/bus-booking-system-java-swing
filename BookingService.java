package BookingSystem;

import java.util.List;

public class BookingService {

    public void book(String user, String bus, int seat, String payment) {
        System.out.println("\n--- TICKET ---");
        System.out.println("User: " + user);
        System.out.println("Bus: " + bus);
        System.out.println("Seat: " + seat);
        System.out.println("Payment: " + payment);
        System.out.println("Status: BOOKED ✅");
    }


}