import java.util.Scanner;

public class BusFareCalculator {

    // Fare rates (₱)
    static final double BASE_FARE          = 13.00;  // first 4 km
    static final double RATE_PER_KM        =  1.80;  // per km beyond 4 km
    static final double STUDENT_DISCOUNT   =  0.20;  // 20%
    static final double SENIOR_PWD_DISCOUNT=  0.20;  // 20%
    static final double AC_SURCHARGE       =  0.25;  // 25% extra for AC bus

    public static double computeBaseFare(double km) {
        if (km <= 0) return 0;
        if (km <= 4) return BASE_FARE;
        return BASE_FARE + ((km - 4) * RATE_PER_KM);
    }

    public static double applyDiscount(double fare, String passengerType) {
        switch (passengerType.toLowerCase()) {
            case "student": return fare * (1 - STUDENT_DISCOUNT);
            case "senior":
            case "pwd":     return fare * (1 - SENIOR_PWD_DISCOUNT);
            default:        return fare;
        }
    }

    public static double applyACBus(double fare, boolean isAC) {
        return isAC ? fare * (1 + AC_SURCHARGE) : fare;
    }

    public static String getPassengerTypeLabel(String type) {
        switch (type.toLowerCase()) {
            case "student": return "Student (20% disc.)";
            case "senior":  return "Senior Citizen (20% disc.)";
            case "pwd":     return "PWD (20% disc.)";
            default:        return "Regular Passenger";
        }
    }

    public static void computeFare(Scanner scanner) {
        System.out.println("\n  ── New Fare Calculation ──");

        System.out.print("  Distance (km): ");
        double km;
        try {
            km = Double.parseDouble(scanner.nextLine().trim());
            if (km <= 0) { System.out.println("  Distance must be > 0."); return; }
        } catch (NumberFormatException e) { System.out.println("  Invalid distance."); return; }

        System.out.println("  Passenger Type:");
        System.out.println("  1. Regular");
        System.out.println("  2. Student");
        System.out.println("  3. Senior Citizen");
        System.out.println("  4. PWD (Person with Disability)");
        System.out.print("  Select (1-4): ");

        String passengerType;
        switch (scanner.nextLine().trim()) {
            case "2": passengerType = "student"; break;
            case "3": passengerType = "senior";  break;
            case "4": passengerType = "pwd";     break;
            default:  passengerType = "regular";
        }

        System.out.print("  Air-conditioned bus? (yes/no): ");
        boolean isAC = scanner.nextLine().trim().equalsIgnoreCase("yes");

        System.out.print("  Number of passengers: ");
        int passengers;
        try {
            passengers = Integer.parseInt(scanner.nextLine().trim());
            if (passengers < 1) { System.out.println("  At least 1 passenger."); return; }
        } catch (NumberFormatException e) { passengers = 1; }

        // Calculations
        double baseFare      = computeBaseFare(km);
        double acFare        = applyACBus(baseFare, isAC);
        double discountedFare= applyDiscount(acFare, passengerType);
        double discountAmt   = acFare - discountedFare;
        double acSurcharge   = isAC ? baseFare * AC_SURCHARGE : 0;
        double totalFare     = discountedFare * passengers;

        // Display receipt
        System.out.println("\n  ╔═════════════════════════════════════════════════╗");
        System.out.println("  ║            🚌 BUS FARE RECEIPT                 ║");
        System.out.println("  ╠═════════════════════════════════════════════════╣");
        System.out.printf("  ║  Distance      : %5.1f km%28s║%n", km, "");
        System.out.printf("  ║  Passenger Type: %-32s║%n", getPassengerTypeLabel(passengerType));
        System.out.printf("  ║  Bus Type      : %-32s║%n", isAC ? "Air-Conditioned (AC)" : "Regular (Non-AC)");
        System.out.printf("  ║  Passengers    : %-32d║%n", passengers);
        System.out.println("  ╠═════════════════════════════════════════════════╣");
        System.out.printf("  ║  Base Fare     : ₱%,11.2f (first 4km: ₱%.2f)  ║%n", baseFare, BASE_FARE);

        if (km > 4) {
            System.out.printf("  ║  Extra km      : %.1f km × ₱%.2f/km = ₱%,.2f%9s║%n",
                    km - 4, RATE_PER_KM, (km - 4) * RATE_PER_KM, "");
        }
        if (isAC) {
            System.out.printf("  ║  AC Surcharge  : ₱%,11.2f (25%%)%14s║%n", acSurcharge, "");
        }
        if (!passengerType.equals("regular")) {
            System.out.printf("  ║  Discount      : ₱%,11.2f (20%% off)%9s║%n", discountAmt, "");
        }
        System.out.println("  ╠═════════════════════════════════════════════════╣");
        System.out.printf("  ║  Fare per person: ₱%,10.2f%17s║%n", discountedFare, "");
        if (passengers > 1) {
            System.out.printf("  ║  × %d passengers  %-31s║%n", passengers, "");
        }
        System.out.printf("  ║  TOTAL FARE    : ₱%,11.2f%17s║%n", totalFare, "");
        System.out.println("  ╚═════════════════════════════════════════════════╝");

        // Tip
        if (!passengerType.equals("regular")) {
            System.out.println("  ℹ️  Bring your valid ID for discounted fare.");
        }
    }

    public static void displayRateTable() {
        System.out.println("\n  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║              📋 FARE RATE TABLE              ║");
        System.out.println("  ╠══════════════════════════════════════════════╣");
        System.out.println("  ║  Base Fare (up to 4 km)  : ₱13.00           ║");
        System.out.println("  ║  Per additional km       : ₱1.80/km         ║");
        System.out.println("  ║  AC Bus surcharge        : +25%             ║");
        System.out.println("  ╠══════════════════════════════════════════════╣");
        System.out.println("  ║  Regular Passenger       : Full fare        ║");
        System.out.println("  ║  Student (w/ valid ID)   : 20% off          ║");
        System.out.println("  ║  Senior Citizen          : 20% off          ║");
        System.out.println("  ║  PWD                     : 20% off          ║");
        System.out.println("  ╠══════════════════════════════════════════════╣");
        System.out.println("  ║  Sample Fares (Regular, Non-AC):             ║");
        double[] samples = {2, 5, 10, 20, 30, 50};
        for (double d : samples) {
            System.out.printf("  ║    %3.0f km  →  ₱%,.2f%23s║%n", d, computeBaseFare(d), "");
        }
        System.out.println("  ╚══════════════════════════════════════════════╝");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         🚌 BUS FARE CALCULATOR            ║");
        System.out.println("╚══════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println("\n  ┌──────────────────────────────┐");
            System.out.println("  │           MENU               │");
            System.out.println("  ├──────────────────────────────┤");
            System.out.println("  │  1. Compute Fare             │");
            System.out.println("  │  2. View Rate Table          │");
            System.out.println("  │  3. Exit                     │");
            System.out.println("  └──────────────────────────────┘");
            System.out.print("  Choice: ");
            switch (scanner.nextLine().trim()) {
                case "1": computeFare(scanner); break;
                case "2": displayRateTable(); break;
                case "3": running = false; System.out.println("\n  Safe travels! 🚌"); break;
                default:  System.out.println("  Invalid choice.");
            }
        }
        scanner.close();
    }
}