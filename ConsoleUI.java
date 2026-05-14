package BookingSystem;

import java.io.*;
import java.util.*;

public class ConsoleUI {

    Scanner sc = new Scanner(System.in);

    Map<String, String> users = new HashMap<>();
    Map<String, Set<Integer>> seatMap = new HashMap<>();

    List<Bus> buses = new ArrayList<>();

    /* ================= USERS ================= */

    void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                users.put(p[0], p[1]);
            }
        } catch (Exception e) {}
    }

    void saveUser(String u, String p) {
        try (FileWriter fw = new FileWriter("users.txt", true)) {
            fw.write(u + "," + p + "\n");
        } catch (Exception e) {}
    }

    /* ================= SEATS ================= */

    void loadSeats() {
        try (BufferedReader br = new BufferedReader(new FileReader("seats.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(":");
                String busName = parts[0];

                Set<Integer> seats = new HashSet<>();

                if (parts.length > 1 && !parts[1].isEmpty()) {
                    String[] arr = parts[1].split(",");

                    for (String s : arr) {
                        seats.add(Integer.parseInt(s));
                    }
                }

                seatMap.put(busName, seats);
            }

        } catch (Exception e) {}
    }

    void saveSeats() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("seats.txt"))) {

            for (String bus : seatMap.keySet()) {

                pw.print(bus + ":");

                Set<Integer> seats = seatMap.get(bus);

                int i = 0;
                for (Integer s : seats) {
                    pw.print(s);
                    if (i < seats.size() - 1) pw.print(",");
                    i++;
                }

                pw.println();
            }

        } catch (Exception e) {}
    }

    /* ================= HISTORY ================= */

    void saveHistory(String user, String bus, int seat, String payment) {
        try (FileWriter fw = new FileWriter("history.txt", true)) {
            fw.write(user + "|" + bus + "|Seat:" + seat + "|" + payment + "\n");
        } catch (Exception e) {}
    }

    void showHistory(String user) {

        System.out.println("\n===== BOOKING HISTORY =====");

        try (BufferedReader br = new BufferedReader(new FileReader("history.txt"))) {

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {

                if (line.startsWith(user + "|")) {
                    System.out.println(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No previous bookings found.");
            }

        } catch (Exception e) {
            System.out.println("No history file found.");
        }
    }

    /* ================= MAIN ================= */

    public void start() {

        loadUsers();
        loadSeats();

        System.out.println("=== BUS BOOKING SYSTEM ===");

        /* ---------- AUTH ---------- */

        System.out.println("\n1. Register");
        System.out.println("2. Login");

        int auth = sc.nextInt();
        sc.nextLine();

        String user;

        if (auth == 1) {

            System.out.print("Create Username: ");
            user = sc.nextLine();

            System.out.print("Create Password: ");
            String pass = sc.nextLine();

            users.put(user, pass);
            saveUser(user, pass);

            System.out.println("Registered Successfully ✅");
        }

        System.out.print("Enter Username: ");
        user = sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        if (!users.containsKey(user) || !users.get(user).equals(pass)) {
            System.out.println("Login Failed ❌");
            return;
        }

        System.out.println("Login Successful ✅");

        /* ---------- SHOW HISTORY ---------- */

        showHistory(user);

        /* ---------- BUS LIST ---------- */

        buses.add(new Bus("APSRTC Express", "Hyderabad → Vijayawada", "Ramesh", 700));
        buses.add(new Bus("Orange Travels", "Hyderabad → Bangalore", "Suresh", 500));
        buses.add(new Bus("SRM Travels", "Chennai → Hyderabad", "Kumar", 800));
        buses.add(new Bus("RedBus", "Chennai → Bangalore", "Kishore", 700));
        buses.add(new Bus("Morning Star", "Hyderabad → Chennai", "Prakash", 900));
        buses.add(new Bus("Zing Bus", "Hyderabad → Tirupati", "Pavan", 500));

        System.out.println("\nAvailable Buses:\n");

        for (int i = 0; i < buses.size(); i++) {
            Bus b = buses.get(i);

            System.out.println((i + 1) + ". " +
                    b.busName + " | " +
                    b.route + " | Driver: " +
                    b.driverName + " | ₹" +
                    b.fare);
        }

        /* ---------- BUS SELECT ---------- */

        System.out.print("\nSelect Bus Number: ");
        int choice = sc.nextInt();

        Bus selected = buses.get(choice - 1);

        /* ---------- SEATS ---------- */

        seatMap.putIfAbsent(selected.busName, new HashSet<>());
        Set<Integer> bookedSeats = seatMap.get(selected.busName);

        System.out.println("\nSeats:");

        for (int i = 1; i <= 10; i++) {
            if (bookedSeats.contains(i)) {
                System.out.print("[X] ");
            } else {
                System.out.print(i + " ");
            }
        }

        System.out.print("\nSelect Seat: ");
        int seat = sc.nextInt();

        if (seat < 1 || seat > 10) {
            System.out.println("Invalid Seat ❌");
            return;
        }

        if (bookedSeats.contains(seat)) {
            System.out.println("Seat Already Booked ❌");
            return;
        }

        bookedSeats.add(seat);
        saveSeats();

        /* ---------- PAYMENT ---------- */

        System.out.println("\n1. UPI  2. Paytm  3. PhonePe");
        int pay = sc.nextInt();

        String payment = (pay == 1) ? "UPI" :
                         (pay == 2) ? "Paytm" : "PhonePe";

        /* ---------- TICKET ---------- */

        System.out.println("\n===== TICKET =====");
        System.out.println("User: " + user);
        System.out.println("Bus: " + selected.busName);
        System.out.println("Route: " + selected.route);
        System.out.println("Seat: " + seat);
        System.out.println("Payment: " + payment);
        System.out.println("Fare: ₹" + selected.fare);
        System.out.println("Status: BOOKED ✅");

        /* ---------- SAVE HISTORY ---------- */

        saveHistory(user, selected.busName, seat, payment);

        /* ---------- FEEDBACK ---------- */

        System.out.print("\nRate Journey (1-5): ");
        int rating = sc.nextInt();

        if (rating >= 4) {
            System.out.println("Excellent ⭐ Thank you!");
        } else {
            System.out.println("We will improve 👍");
        }
    }
}