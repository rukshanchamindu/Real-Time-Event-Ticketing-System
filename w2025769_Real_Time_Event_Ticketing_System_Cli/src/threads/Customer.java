package threads;
import core.TicketPool;
import core.Ticket;

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
        while (true) {
            synchronized (ticketPool) {  // Ensure synchronization with the ticket pool
                // If no tickets are available, the customer will wait
                while (ticketPool.getCurrentTicketCount() == 0) {
                    try {
//                        System.out.println("Customer " + customerId + " could not purchase a ticket (no tickets available).");
                        ticketPool.wait();  // Wait for new tickets to be added
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();  // Handle thread interruption
                    }
                }

                // Now that the customer is awake, attempt to buy a ticket
                Ticket ticket = ticketPool.removeTicket();
                if (ticket == null) {
                    System.out.println("ERROR: Customer " + customerId + " could not purchase a ticket (ticket is null).");
                } else {
                    System.out.println("Customer " + customerId + " purchased: " + ticket);
                }
            }

            // Sleep for the specified retrieval interval before attempting to buy another ticket
            try {
                Thread.sleep(retrievalInterval*1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Handle interruption
                // Once the vendor thread exits, log its termination
                System.out.println("Customer " + customerId + " is no longer active.");
            }
        }

    }
}
