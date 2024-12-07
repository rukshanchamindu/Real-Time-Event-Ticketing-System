package core;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private Queue<Ticket> ticketQueue;  // Queue to hold tickets
    private int maxTicketCapacity;      // Maximum capacity of the ticket pool
    private boolean debug;                    // Debug flag to control debug messages

    public TicketPool(int maxTicketCapacity, boolean debug) {
        this.ticketQueue = new LinkedList<>();
        this.maxTicketCapacity = maxTicketCapacity;
        this.debug = debug;
            System.out.println("Ticket Pool initialized");
    }

    // Synchronized method to add tickets to the pool (for vendors)
    public synchronized boolean addTickets(int quantity) {
        if (quantity <= 0) {
            if (debug) {
                System.out.println("Cannot add a non-positive number of tickets.");
            }
            return false;
        }

        // Check if there is enough space in the pool
        if (ticketQueue.size() + quantity <= maxTicketCapacity) {
            for (int i = 0; i < quantity; i++) {
                ticketQueue.add(new Ticket());  // Add a new ticket to the pool
            }
            if (debug) {
                System.out.println(quantity + " tickets have been added to the pool.");
            }
                // Notify all waiting consumers (customers) that tickets are now available
                notifyAll();  // Wake up all waiting customers
            return true;
        } else {
            if (debug) {
                System.out.println("Ticket pool is full. Cannot add more tickets.");
            }
            return false;
        }
    }

    // Synchronized method to remove tickets from the pool (for customers)
    public synchronized Ticket removeTicket() {
        if (ticketQueue.isEmpty()) {
            if (debug) {
                System.out.println("No tickets available.");
            }
            return null;  // No tickets to purchase
        } else {
            Ticket ticket = ticketQueue.poll();  // Remove a ticket from the pool
            if (debug) {
                System.out.println("Customer purchased: " + ticket);
            }
            return ticket;
        }
    }

    // Get the current number of tickets in the pool
    public synchronized int getCurrentTicketCount() {
        return ticketQueue.size();
    }

    // Get the maximum capacity of the pool
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
}
