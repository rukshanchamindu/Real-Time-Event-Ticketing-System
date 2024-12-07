package core;

public  class Ticket {
    private static int ticketIdCounter = 1;
    private final int ticketId;

    public Ticket() {
        this.ticketId = ticketIdCounter++;  // Assign a unique ticket ID
    }

    public int getTicketId() {
        return ticketId;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketId;
    }
}