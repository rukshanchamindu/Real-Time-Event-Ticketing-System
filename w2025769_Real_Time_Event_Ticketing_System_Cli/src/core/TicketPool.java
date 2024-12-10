package core;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<Ticket> ticketQueue;  // Queue to hold tickets
    private final int maximumTicketCapacity;  // Maximum capacity of the ticket pool
    private int totalTickets;                 // Total number of tickets available in the pool
    private int ticketIdCounter;              // Counter to generate unique ticket IDs
    private final boolean debug;              // Debug flag to control debug messages

    public TicketPool(int maximumTicketCapacity, int totalTickets, boolean debug) {
        this.ticketQueue = new LinkedList<>();
        this.maximumTicketCapacity = maximumTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketIdCounter = 1;  // Start ticket ID counter from 1
        this.debug = debug;
        System.out.println("Ticket Pool initialized with total tickets: " + totalTickets);
    }

    // Method to add tickets to the pool (for vendors)
    public synchronized boolean addTickets(int quantity) {
        // Wait if the pool is full (i.e., ticketQueue size is equal to the max capacity)
        while (ticketQueue.size() >= maximumTicketCapacity) {
            try {
                wait();  // Wait until space is available in the pool
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Handle interruption
                return false;
            }
        }

        // Add tickets to the pool and decrement the total available tickets
        for (int i = 0; i < quantity; i++) {
            ticketQueue.add(new Ticket(generateTicketId()));  // Add a ticket with a unique ID
            totalTickets--;  // Decrease the total ticket count
        }
        notifyAll();  // Notify waiting customers that tickets are available

        if (debug) {
            System.out.println(quantity + " tickets have been added to the pool. Total remaining: " + totalTickets);
        }
        return true;
    }

    // Method for customers to buy tickets (removes tickets from the pool)
    public synchronized Ticket removeTicket() {
        // Stop customer threads when no tickets are left
        if (totalTickets == 0 && ticketQueue.isEmpty()) {
            notifyAll(); // Notify all threads to stop
            return null;  // No tickets available
        }

        while (ticketQueue.isEmpty()) {
            try {
                wait();  // Wait until tickets are available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Handle interruption
                return null;
            }
        }

        Ticket ticket = ticketQueue.poll();  // Remove a ticket from the pool
        notifyAll();  // Notify vendors that space is available for new tickets
        if (debug) {
            System.out.println("Customer bought a ticket: " + ticket);
        }
        return ticket;
    }

    // Get the total tickets count
    public synchronized int getTotalTicketCount() {
        return totalTickets;
    }

    // Method to generate a unique ticket ID
    private synchronized int generateTicketId() {
        return ticketIdCounter++;
    }
}
