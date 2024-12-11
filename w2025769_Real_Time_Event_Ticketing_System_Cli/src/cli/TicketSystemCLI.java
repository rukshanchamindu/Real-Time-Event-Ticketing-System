package cli;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import config.Configuration;
import threads.Vendor;
import threads.Customer;
import core.TicketPool;
import logger.LoggerService;  // Import the LoggerService

public class TicketSystemCLI {
    private static boolean isRunning = false; // Flag to indicate if the system is running
    private static List<Thread> vendorThreads = new ArrayList<>();
    private static List<Thread> customerThreads = new ArrayList<>();
    private static Configuration config = null;
    private static TicketPool ticketPool = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Print the welcome message
            LoggerService.log("Welcome to the Ticketing System!");
            System.out.println("Welcome to the Ticketing System!");
            System.out.println("=================================");

            // Ask the user if they want to load an old config or create a new one
            System.out.println("\nDo you want to load an existing configuration or create a new one?");
            System.out.println("1. Load existing configuration");
            System.out.println("2. Create new configuration");
//            System.out.print("Enter your choice (1 or 2): ");
            int choice =  getValidatedInput(scanner, "Enter your choice (1 or 2): ", 1,2 );
            scanner.nextLine();

            // Use switch-case to handle user choice
            switch (choice) {
                case 1:
                    config = Configuration.loadConfig();

                    if (config == null) {
                        System.out.println("Invalid configuration loaded. Please provide a new configuration.");
                        LoggerService.log("Invalid configuration loaded.");
                        config = createNewConfiguration(scanner);
                    } else {
                        System.out.println("Loaded configuration successfully!");
                        LoggerService.log("Loaded configuration successfully.");
                    }
                    break;

                case 2:
                    // Create a new configuration
                    config = createNewConfiguration(scanner);
                    LoggerService.log("Created new configuration.");
                    break;

                default:
                    System.out.println("Invalid choice. Exiting.");
                    LoggerService.log("Invalid choice entered. Exiting.");
                    return;
            }

            // Saving New configuration
            config.saveConfig();
            LoggerService.log("Configuration saved.");

            // Display the loaded or created configuration
            System.out.println("\nConfiguration Details:");
            System.out.println("Total Ticket Count: " + config.getTotalTickets());
            System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());
            System.out.println("Tickets Per Release: " + config.getTicketsPerRelease());
            System.out.println("Ticket Release Interval: " + config.getTicketReleaseInterval());
            System.out.println("Customer Retrieval Interval: " + config.getCustomerRetrievalInterval());
            System.out.println("Vendor Count: " + config.getVendorCount());
            System.out.println("Customer Count: " + config.getCustomerCount());
            if (config.isDebug()) {
                System.out.println("Debug: " + (config.isDebug() ? "Enabled" : "Disabled"));
            }

            // Ask the user to start, stop, or exit the simulation
            while (true) {
                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Start the system");
                System.out.println("2. Stop the system");
                System.out.println("3. Exit the system");
//                System.out.print("Enter your choice (1, 2, or 3): ");
                int actionChoice =  getValidatedInput(scanner, "Enter your choice (1, 2, or 3): ", 1, 3);
                scanner.nextLine(); // Consume the newline character

                switch (actionChoice) {
                    case 1:
                        if (!isRunning) {
                            startSystem(config);
                            LoggerService.log("System started.");
                        } else {
                            System.out.println("The system is already running.");
                            LoggerService.log("System is already running.");
                        }
                        break;

                    case 2:
                        if (isRunning) {
                            stopSystem();
                            LoggerService.log("System stopped.");
                        } else {
                            System.out.println("The system is not running.");
                        }
                        break;

                    case 3:
                        if (isRunning) {
                            stopSystem();
                        }
                        System.out.println("Exiting the system...");
                        LoggerService.log("Exiting the system.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        LoggerService.log("Invalid menu choice.");
                }

                // Check if there are no tickets left, and notify user
                if (ticketPool != null && ticketPool.getTotalTicketCount() == 0) {
                    System.out.println("No tickets available. The system has no more tickets to sell.");
                    LoggerService.log("No tickets available. The system has no more tickets to sell.");
                    System.out.println("You can reset the system or exit.");
                }
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            System.out.println("An unexpected error occurred: " + e.getMessage());
            LoggerService.log("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Function to create a new configuration
    private static Configuration createNewConfiguration(Scanner scanner) {
        System.out.println("\nEnter the following details for the new configuration:");

        int totalTickets = getValidatedInput(scanner, "Total Ticket Count : ", 1, Integer.MAX_VALUE);
        int maxTicketCapacity = getValidatedInput(scanner, "Max Ticket Pool Capacity : ", 1, Integer.MAX_VALUE);
        int ticketReleaseInterval = getValidatedInput(scanner, "Ticket Release Interval (in seconds) : ", 1, Integer.MAX_VALUE);
        int ticketsPerRelease = getValidatedInput(scanner, "Tickets Per Release : ", 0, Integer.MAX_VALUE);
        int customerRetrievalInterval = getValidatedInput(scanner, "Customer Retrieval Interval (in seconds) : ", 1, Integer.MAX_VALUE);
        int vendorCount = getValidatedInput(scanner, "Vendor Count (at least 1) : ", 1, Integer.MAX_VALUE);
        int customerCount = getValidatedInput(scanner, "Customer Count (at least 1): ", 1, Integer.MAX_VALUE);

        boolean debug = false;

        return new Configuration(totalTickets, ticketsPerRelease, ticketReleaseInterval, customerRetrievalInterval, maxTicketCapacity, vendorCount, customerCount, debug);
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

    // Function to start the system (initialize and start vendor/customer threads)
    private static void startSystem(Configuration config) {
        isRunning = true;

        // Initialize TicketPool
        ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets(), config.isDebug());

        // Calculate tickets per vendor ( remainder handled by the last vendor)
        int totalTickets = config.getTotalTickets(); // Assume total tickets = tickets per release * vendor count
        int ticketsPerVendor = totalTickets / config.getVendorCount();  // Base number of tickets per vendor
        int remainingTickets = totalTickets % config.getVendorCount();  // Remainder tickets

        // Create and start vendor threads
        for (int i = 0; i < config.getVendorCount(); i++) {
            String vendorId = "Vendor-" + (i + 1);

            int ticketsForThisVendor;
            if (i == config.getVendorCount() - 1) {
                ticketsForThisVendor = ticketsPerVendor + remainingTickets;
            } else {
                ticketsForThisVendor = ticketsPerVendor;
            }

            Vendor vendor = new Vendor(ticketPool, vendorId, ticketsForThisVendor, config.getTicketsPerRelease(), config.getTicketReleaseInterval());
            Thread vendorThread = new Thread(vendor, vendorId);
            vendorThread.start();
            vendorThreads.add(vendorThread);
        }

        // Create and start customer threads
        for (int i = 0; i < config.getCustomerCount(); i++) {
            String customerId = "Customer-" + (i + 1);
            Customer customer = new Customer(ticketPool, customerId, config.getCustomerRetrievalInterval());
            Thread customerThread = new Thread(customer, customerId);
            customerThread.start();
            customerThreads.add(customerThread);
        }

        System.out.println("System started! Vendors and customers are now active.");
        LoggerService.log("System started! Vendors and customers are now active.");
    }

    // Function to stop the system (interrupt threads)
    private static void stopSystem() {
        isRunning = false;
        // Interrupt the threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.interrupt();  // Interrupt vendor thread
        }
        for (Thread customerThread : customerThreads) {
            customerThread.interrupt();  // Interrupt customer thread
        }

        System.out.println("System stopped successfully.");
        LoggerService.log("System stopped successfully.");
    }
}
