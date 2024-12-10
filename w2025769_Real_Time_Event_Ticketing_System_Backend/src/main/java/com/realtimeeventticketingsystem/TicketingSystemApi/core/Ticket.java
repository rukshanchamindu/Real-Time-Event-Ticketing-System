package com.realtimeeventticketingsystem.TicketingSystemApi.core;

public class Ticket {
    private final int ticketId;  // Unique ticket ID assigned by TicketPool

    public Ticket(int ticketId) {
        this.ticketId = ticketId;  // Assign the ticket ID provided by TicketPool
    }

    public int getTicketId() {
        return ticketId;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketId;
    }
}
