package com.realtimeeventticketingsystem.TicketingSystemApi.config;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    private int totalTickets;
    private int ticketsPerRelease;
    private int ticketReleaseInterval;
    private int customerRetrievalInterval;
    private int maxTicketCapacity;
    private int vendorCount;  // Number of vendors
    private int customerCount;  // Number of customers
    private boolean debug;  // Debug flag

    // Constructor with validation
    public Configuration(int totalTickets, int ticketsPerRelease, int ticketReleaseRate, int customerRetrievalRate,
                         int maxTicketCapacity, int vendorCount, int customerCount, boolean debug) {
        this.totalTickets = totalTickets;
        this.ticketsPerRelease = ticketsPerRelease;
        this.ticketReleaseInterval = ticketReleaseRate;
        this.customerRetrievalInterval = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.vendorCount = vendorCount;
        this.customerCount = customerCount;
        this.debug = debug;
    }

    // Getters for all fields
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketsPerRelease() {
        return ticketsPerRelease;
    }

    public int getTicketReleaseInterval() {
        return ticketReleaseInterval;
    }

    public int getCustomerRetrievalInterval() {
        return customerRetrievalInterval;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public int getVendorCount() {
        return vendorCount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public boolean isDebug() {
        return debug;
    }

    // Save configuration to a JSON file and return whether it was successful
    public boolean saveConfig() {
        String fileName = "config.json";
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);  // Convert the object into JSON and write it to the file
            if (debug) {
                System.out.println("Configuration saved to " + fileName);
            }
            return true;  // Indicating successful save
        } catch (IOException e) {
            if (debug) {
                System.out.println("Failed to save configuration to " + fileName);
                e.printStackTrace();
            }
            return false;  // Indicating failure to save
        }
    }

    // Load configuration from a JSON file
    public static Configuration loadConfig() {
        String fileName = "config.json";
        Gson gson = new Gson();
        Configuration config = null;

        try (FileReader reader = new FileReader(fileName)) {
            config = gson.fromJson(reader, Configuration.class);  // Convert JSON from file back into the Configuration object
        } catch (IOException e) {
                System.out.println("Failed to load configuration from " + fileName);
        }

        // If configuration is valid, return it, otherwise return null
        if (config != null && !config.validateConfiguration()) {
            return null;  // If configuration is invalid, return null
        }

        return config;
    }

    // Validate loaded configuration (does not reset values, just returns false if invalid)
    public boolean validateConfiguration() {
        boolean isValid = true;

        // Validate each parameter and print debug messages if enabled
        if (this.ticketsPerRelease < 0) {
            if (debug) {
                System.out.println("Warning: Invalid value for tickets per release. Must be non-negative.");
            }
            isValid = false;
        }

        if (this.ticketReleaseInterval <= 0) {
            if (debug) {
                System.out.println("Warning: Invalid value for ticket release interval. Must be greater than zero.");
            }
            isValid = false;
        }

        if (this.customerRetrievalInterval <= 0) {
            if (debug) {
                System.out.println("Warning: Invalid value for customer retrieval interval. Must be greater than zero.");
            }
            isValid = false;
        }

        if (this.maxTicketCapacity <= 0) {
            if (debug) {
                System.out.println("Warning: Invalid value for max ticket capacity. Must be greater than zero.");
            }
            isValid = false;
        }

        if (this.vendorCount < 1) {
            if (debug) {
                System.out.println("Warning: Invalid vendor count. Must be at least 1.");
            }
            isValid = false;
        }

        if (this.customerCount < 1) {
            if (debug) {
                System.out.println("Warning: Invalid customer count. Must be at least 1.");
            }
            isValid = false;
        }

        if (isValid && debug) {
            System.out.println("Configuration loaded successfully.");
        }

        return isValid;  // Return true if all values are valid, false if invalid
    }

    // Return the configuration as a JSON string
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);  // Convert the object to JSON string
    }
}
