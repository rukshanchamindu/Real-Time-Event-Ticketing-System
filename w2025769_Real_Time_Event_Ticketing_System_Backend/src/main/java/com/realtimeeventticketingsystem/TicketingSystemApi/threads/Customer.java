package com.realtimeeventticketingsystem.TicketingSystemApi.threads;

import com.realtimeeventticketingsystem.TicketingSystemApi.core.TicketPool;
import com.realtimeeventticketingsystem.TicketingSystemApi.core.Ticket;
import com.realtimeeventticketingsystem.TicketingSystemApi.logger.LoggerService;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;      // Unique customer identifier
    private final int retrievalInterval;  // Interval (in ms) at which customers attempt to buy tickets

    // Constructor to initialize the Customer with necessary attributes
    public Customer(TicketPool ticketPool, String customerId, int retrievalInterval) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // Attempt to buy a ticket directly
            Ticket ticket = ticketPool.removeTicket();

            if (ticket == null) {
                // Log the case where no tickets are available
                LoggerService.log("Customer " + customerId + " could not purchase a ticket (no tickets available).");
                System.out.println("Stopping Customer thread: " + customerId + " as no tickets are available.");
                break;  // Exit the loop when no tickets are available
            } else {
                // Log when a customer purchases a ticket
                LoggerService.log("Customer " + customerId + " purchased: " + ticket);
                System.out.println("Customer " + customerId + " purchased: " + ticket);
            }

            // Sleep for the specified retrieval interval before attempting to buy another ticket
            try {
                Thread.sleep(retrievalInterval * 1000);  // Convert seconds to milliseconds
            } catch (InterruptedException e) {
                // Log the interruption event
                LoggerService.log("Customer " + customerId + " was interrupted.");
                Thread.currentThread().interrupt();  // Handle interruption
                System.out.println("Customer " + customerId + " is no longer active.");
                break;  // Exit the loop when interrupted
            }
        }
    }
}
