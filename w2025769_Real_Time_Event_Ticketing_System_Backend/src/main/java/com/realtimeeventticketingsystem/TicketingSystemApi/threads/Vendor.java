package com.realtimeeventticketingsystem.TicketingSystemApi.threads;

import com.realtimeeventticketingsystem.TicketingSystemApi.core.TicketPool;
import com.realtimeeventticketingsystem.TicketingSystemApi.core.Ticket;
import com.realtimeeventticketingsystem.TicketingSystemApi.logger.LoggerService;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final String vendorId;          // Unique vendor identifier
    private int remainingTickets;           // Remaining tickets the vendor can release
    private final int ticketsPerRelease;    // Number of tickets the vendor releases at a time
    private final int releaseInterval;      // Interval (in seconds) at which tickets are released

    // Constructor to initialize the Vendor with necessary attributes
    public Vendor(TicketPool ticketPool, String vendorId, int ticketsForThisVendor,
                  int ticketsPerRelease, int releaseInterval) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.remainingTickets = ticketsForThisVendor;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        // Vendor releases tickets until they run out
        while (remainingTickets > 0) {
            // Calculate how many tickets to release (up to ticketsPerRelease)
            int ticketsToRelease = Math.min(remainingTickets, ticketsPerRelease);

            // Attempt to add tickets to the pool
            boolean added = ticketPool.addTickets(ticketsToRelease);

            // Log the vendor activity
            if (added) {
                remainingTickets -= ticketsToRelease;  // Decrease the number of tickets left
                // Log ticket release and remaining tickets
                LoggerService.log("Vendor " + vendorId + " released " + ticketsToRelease + " tickets. Remaining tickets: " + remainingTickets);
                System.out.println("Vendor " + vendorId + " released " + ticketsToRelease + " tickets. Remaining tickets: " + remainingTickets + ".");
            }

            // Sleep for the specified release interval before releasing more tickets
            try {
                Thread.sleep(releaseInterval * 1000);  // Convert seconds to milliseconds
            } catch (InterruptedException e) {
                // Log interruption and exit
                LoggerService.log("Vendor " + vendorId + " was interrupted.");
                Thread.currentThread().interrupt();  // Handle interruption
                break;  // Exit the loop if interrupted
            }
        }

        // Once the vendor has no tickets left, log its termination and exit
        LoggerService.log("Vendor " + vendorId + " has finished releasing all tickets.");
        System.out.println("Vendor " + vendorId + " has finished releasing all tickets.");
    }
}
