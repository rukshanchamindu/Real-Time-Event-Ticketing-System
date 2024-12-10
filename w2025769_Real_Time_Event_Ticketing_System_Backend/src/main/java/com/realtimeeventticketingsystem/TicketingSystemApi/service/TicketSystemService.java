package com.realtimeeventticketingsystem.TicketingSystemApi.service;


import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import com.realtimeeventticketingsystem.TicketingSystemApi.config.Configuration;
import com.realtimeeventticketingsystem.TicketingSystemApi.core.TicketPool;
import com.realtimeeventticketingsystem.TicketingSystemApi.threads.Vendor;
import com.realtimeeventticketingsystem.TicketingSystemApi.threads.Customer;
import com.realtimeeventticketingsystem.TicketingSystemApi.logger.LoggerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketSystemService {

    private boolean isRunning = false;
    private static Configuration config = null;

    private List<Thread> vendorThreads = new ArrayList<>();
    private List<Thread> customerThreads = new ArrayList<>();
    private TicketPool ticketPool = null;

    // Method to start the system
    public boolean startSystem() {
        if (!isRunning) {

            // Initialize TicketPool
            ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets(), config.isDebug());

            // Calculate tickets per vendor (remainder handled by the last vendor)
            int totalTickets = config.getTotalTickets();
            int ticketsPerVendor = totalTickets / config.getVendorCount();
            int remainingTickets = totalTickets % config.getVendorCount();

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
            isRunning = true;
            // Log system start
            System.out.println("System started! Vendors and customers are now active.");
            LoggerService.log("System started! Vendors and customers are now active.");
            return true;
        } else {
            System.out.println("The system is already running.");
            return false;
        }
    }

    // Method to stop the system (interrupt threads)
    public boolean stopSystem() {
        if (isRunning) {
            // Interrupt vendor threads
            for (Thread vendorThread : vendorThreads) {
                vendorThread.interrupt();
            }

            // Interrupt customer threads
            for (Thread customerThread : customerThreads) {
                customerThread.interrupt();
            }

            System.out.println("System stopped successfully.");
            LoggerService.log("System stopped successfully.");
            isRunning = false;
            return true;
        } else {
            System.out.println("The system is not running.");
            return false;
        }
    }

    // Method to load configuration and return as JSON
    public String getConfiguration() {
        // Load the configuration
        config = Configuration.loadConfig();

        // Return the configuration as a JSON string
        return config != null ? config.toJson() : null;  // If config is not null, convert it to JSON
    }

    // Method to set configuration from JSON string
    public boolean setConfiguration(String configData) {
        Gson gson = new Gson();
        try {
            // Deserialize the JSON into the Configuration object
            Configuration newConfig = gson.fromJson(configData, Configuration.class);

            // Validate the loaded configuration
            if (newConfig != null && newConfig.validateConfiguration()) {
                // Set the loaded configuration
                config = newConfig;

                // Optionally, save the configuration to a file
                config.saveConfig();

                // Log the successful setting of configuration
                System.out.println("Configuration set successfully: " + configData);
                LoggerService.wipeLogFile();
                return true;
            } else {
                System.out.println("Invalid configuration data: " + configData);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error while setting configuration from JSON: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
