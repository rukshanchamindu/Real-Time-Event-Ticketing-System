package cli;
import java.util.Scanner;
import config.Configuration;

public class TicketSystemCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration config = null;

        try {
            // Step 1: Print the welcome message
            System.out.println("Welcome to the Ticketing System!");
            System.out.println("=================================");

            // Step 2: Ask the user if they want to load an old config or create a new one
            System.out.println("\nDo you want to load an existing configuration or create a new one?");
            System.out.println("1. Load existing configuration");
            System.out.println("2. Create new configuration");
            System.out.print("Enter your choice (1 or 2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Step 3: Use switch-case to handle user choice
            switch (choice) {
                case 1:
                    config = Configuration.loadConfig();

                    if (config == null) {
                        System.out.println("Invalid configuration loaded. Please provide a new configuration.");
                        config = createNewConfiguration(scanner);
                    } else {
                        System.out.println("Loaded configuration successfully!");
                    }
                    break;

                case 2:
                    // Create a new configuration
                    config = createNewConfiguration(scanner);
                    break;

                default:
                    System.out.println("Invalid choice. Exiting.");
                    return;
            }

            // Step 4: Display the loaded or created configuration
            System.out.println("\nConfiguration Details:");
            System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());
            System.out.println("Tickets Per Release: " + config.getTicketsPerRelease());
            System.out.println("Ticket Release Interval: " + config.getTicketReleaseInterval());
            System.out.println("Customer Retrieval Interval: " + config.getCustomerRetrievalInterval());
            System.out.println("Vendor Count: " + config.getVendorCount());
            System.out.println("Customer Count: " + config.getCustomerCount());
            if (config.isDebug()) {
                System.out.println("Debug: " + (config.isDebug() ? "Enabled" : "Disabled"));
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Function to create a new configuration
    private static Configuration createNewConfiguration(Scanner scanner) {
        // Create a new configuration
        System.out.println("\nEnter the following details for the new configuration:");

        // Get max ticket capacity
        int maxTicketCapacity = getValidatedInput(scanner, "Max Ticket Capacity : ", 1, Integer.MAX_VALUE);
        // Get tickets per release
        int ticketsPerRelease = getValidatedInput(scanner, "Tickets Per Release : ", 0, Integer.MAX_VALUE);
        // Get ticket release interval
        int ticketReleaseInterval = getValidatedInput(scanner, "Ticket Release Interval (in seconds) : ", 1, Integer.MAX_VALUE);
        // Get customer retrieval interval
        int customerRetrievalInterval = getValidatedInput(scanner, "Customer Retrieval Interval (in seconds) : ", 1, Integer.MAX_VALUE);
        // Get vendor count
        int vendorCount = getValidatedInput(scanner, "Vendor Count (at least 1) : ", 1, Integer.MAX_VALUE);
        // Get customer count
        int customerCount = getValidatedInput(scanner, "Customer Count (at least 1): ", 1, Integer.MAX_VALUE);

        boolean debug = false;

        // Create the configuration object
        return new Configuration(ticketsPerRelease, ticketReleaseInterval, customerRetrievalInterval, maxTicketCapacity, vendorCount, customerCount, debug);
    }

    // Helper function to get validated user input
    private static int getValidatedInput(Scanner scanner, String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                if (scanner.hasNextInt()) {
                    input = scanner.nextInt();
                    if (input >= min && input <= max) {
                        break;  // Valid input
                    } else {
                        System.out.println("Input must be between " + min + " and " + max + ". Try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.next();  // Consume invalid input
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();  // Consume invalid input
            }
        }
        return input;
    }
}
