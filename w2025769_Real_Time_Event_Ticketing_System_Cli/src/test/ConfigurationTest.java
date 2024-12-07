package test;

import java.io.File;
import java.io.IOException;
import config.Configuration;

public class ConfigurationTest {
    public static void main(String[] args) {
        // Test Configuration: create a Configuration object with sample data
        System.out.println("Creating and saving configuration...");
        Configuration config = new Configuration(
                100,  // ticketsPerRelease
                5,    // ticketReleaseInterval (in seconds)
                3,    // customerRetrievalInterval (in seconds)
                1000, // maxTicketCapacity
                2,    // vendorCount
                10,   // customerCount
                true  // debug = true
        );

        // Save the configuration to a file

        boolean saveSuccess = config.saveConfig();

        if (saveSuccess) {
            System.out.println("Configuration saved successfully");
        } else {
            System.out.println("Failed to save configuration.");
            return;  // Exit if save failed
        }

        // Test Loading Configuration: Load the configuration from the file
        System.out.println("\nLoading configuration...");
        Configuration loadedConfig = Configuration.loadConfig();

        if (loadedConfig == null) {
            System.out.println("Failed to load configuration.");
            return;  // Exit if load failed
        }

        // Check loaded configuration values
        System.out.println("\nLoaded Configuration:");
        System.out.println("Tickets Per Release: " + loadedConfig.getTicketsPerRelease());
        System.out.println("Ticket Release Interval: " + loadedConfig.getTicketReleaseInterval());
        System.out.println("Customer Retrieval Interval: " + loadedConfig.getCustomerRetrievalInterval());
        System.out.println("Max Ticket Capacity: " + loadedConfig.getMaxTicketCapacity());
        System.out.println("Vendor Count: " + loadedConfig.getVendorCount());
        System.out.println("Customer Count: " + loadedConfig.getCustomerCount());

        // Test configuration validation
        System.out.println("\nValidating Configuration...");
        boolean isValid = loadedConfig.validateConfiguration();

        if (isValid) {
            System.out.println("Configuration is valid.");
        } else {
            System.out.println("Configuration is invalid.");
        }

    }
}
