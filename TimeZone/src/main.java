import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class main {
    private static final Map<String, String> TIME_ZONES = new LinkedHashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    static {
        TIME_ZONES.put("New York", "America/New_York");
        TIME_ZONES.put("London", "Europe/London");
        TIME_ZONES.put("Tokyo", "Asia/Tokyo");
        TIME_ZONES.put("Sydney", "Australia/Sydney");
    }

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    displayClocks();
                    break;
                case 2:
                    addTimeZone();
                    break;
                case 3:
                    removeTimeZone();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Thank you for using the Advanced Multi-Timezone Clock!");
    }

    private static void displayMenu() {
        System.out.println("\n=== Advanced Multi-Timezone Clock ===");
        System.out.println("1. Display Clocks");
        System.out.println("2. Add Time Zone");
        System.out.println("3. Remove Time Zone");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid number. Please try again.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void displayClocks() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int displayDuration = 20; // Display for 20 seconds

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < displayDuration * 1000) {
            clearConsole();
            System.out.println("=== Current Time in Various Time Zones ===");
            for (Map.Entry<String, String> entry : TIME_ZONES.entrySet()) {
                ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(entry.getValue()));
                String time = zonedDateTime.format(formatter);
                String asciiClock = getAsciiClock(zonedDateTime);
                System.out.printf("%s (%s):%n%s%n%s%n%n", entry.getKey(), entry.getValue(), time, asciiClock);
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getAsciiClock(ZonedDateTime time) {
        int hour = time.getHour() % 12;
        int minute = time.getMinute();
        String[] clock = {
                "  ┌─────────┐  ",
                "  │         │  ",
                "  │         │  ",
                "  │         │  ",
                "  │         │  ",
                "  └─────────┘  "
        };
        double hourAngle = Math.toRadians((hour + minute / 60.0) * 30 - 90);
        double minuteAngle = Math.toRadians(minute * 6 - 90);
        clock = drawHand(clock, 3, 3, hourAngle, 2, '•');
        clock = drawHand(clock, 3, 3, minuteAngle, 3, '•');
        return String.join("\n", clock);
    }

    private static String[] drawHand(String[] clock, int centerX, int centerY, double angle, int length, char symbol) {
        int endX = centerX + (int) Math.round(Math.cos(angle) * length);
        int endY = centerY + (int) Math.round(Math.sin(angle) * length);
        String[] newClock = Arrays.copyOf(clock, clock.length);
        newClock[endY] = newClock[endY].substring(0, endX) + symbol + newClock[endY].substring(endX + 1);
        return newClock;
    }

    private static void addTimeZone() {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter the name for the new time zone: ");
        String name = scanner.nextLine();
        System.out.print("Enter the time zone ID (e.g., America/Chicago): ");
        String zoneId = scanner.nextLine();
        try {
            ZoneId.of(zoneId); // Validate the zone ID
            TIME_ZONES.put(name, zoneId);
            System.out.println("Time zone added successfully!");
        } catch (Exception e) {
            System.out.println("Invalid time zone ID. The time zone was not added.");
        }
    }

    private static void removeTimeZone() {
        scanner.nextLine(); // Consume newline
        System.out.println("Current time zones:");
        for (String name : TIME_ZONES.keySet()) {
            System.out.println("- " + name);
        }
        System.out.print("Enter the name of the time zone to remove: ");
        String name = scanner.nextLine();
        if (TIME_ZONES.remove(name) != null) {
            System.out.println("Time zone removed successfully!");
        } else {
            System.out.println("Time zone not found.");
        }
    }

    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            // If console clearing fails, just print newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}