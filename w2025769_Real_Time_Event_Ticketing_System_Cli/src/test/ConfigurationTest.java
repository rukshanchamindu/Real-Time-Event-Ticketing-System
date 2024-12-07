package test;

import java.io.File;
import java.io.IOException;
import config.Configuration;

public class ConfigurationTest {
    public static void main(String[] args) {
        // Step 1: Create a new Configuration object with sample values
        Configuration config = new Configuration(200, 15, 5, 3, 100, 5, 50, true);

        // Step 2: Save the configuration to a file
        boolean isSaved = config.saveConfig();
        if (isSaved) {
            System.out.println("Configuration saved successfully.");
        } else {
            System.out.println("Failed to save configuration.");
        }

        // Step 3: Print the configuration values using getters
        System.out.println("\nConfiguration Details:");
        System.out.println("Total Tickets: " + config.getTotalTickets());
        System.out.println("Tickets Per Release: " + config.getTicketsPerRelease());
        System.out.println("Ticket Release Interval: " + config.getTicketReleaseInterval());
        System.out.println("Customer Retrieval Interval: " + config.getCustomerRetrievalInterval());
        System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());
        System.out.println("Vendor Count: " + config.getVendorCount());
        System.out.println("Customer Count: " + config.getCustomerCount());
        System.out.println("Debug Mode: " + config.isDebug());
    }
}
