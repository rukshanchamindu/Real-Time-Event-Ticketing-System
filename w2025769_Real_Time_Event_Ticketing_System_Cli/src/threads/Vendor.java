package threads;
import core.TicketPool;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final String vendorId;          // Unique vendor identifier
    private int remainingTickets;           // Remaining tickets the vendor can release
    private final int ticketsPerRelease;    // Number of tickets the vendor releases at a time
    private final int releaseInterval;      // Interval (in ms) at which tickets are released

    // Constructor to initialize the Vendor with necessary attributes
    public Vendor(TicketPool ticketPool, String vendorId, int ticketsForThisVendor,
                  int ticketsPerRelease, int releaseInterval) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.remainingTickets = ticketsForThisVendor;  // Set initial tickets to release
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
    }

    // Synchronized method to dynamically add tickets to the vendor's count
    public synchronized void addTickets(int additionalTickets) {
        if (additionalTickets > 0) {
            remainingTickets += additionalTickets;
            notifyAll();
            System.out.println("Vendor " + vendorId + " received " + additionalTickets + " additional tickets.");
        } else {
            System.out.println("Vendor " + vendorId + " cannot receive a non-positive number of tickets.");
        }
    }

    @Override
    public void run() {
        while (true) {  // Keep running to check for new tickets or release current tickets
            synchronized (this) {
                // If no tickets are left, the vendor will pause but keep running
                if (remainingTickets <= 0) {
                    try {
                        System.out.println("Vendor " + vendorId + " is waiting for additional tickets.");
                        wait();  // Wait for new tickets to be added
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;  // Exit the loop if interrupted
                    }
                }
            }

            // Calculate how many tickets to release (vendor can release a max of ticketsPerRelease at a time)
            int ticketsToRelease = Math.min(remainingTickets, ticketsPerRelease);
            boolean added = ticketPool.addTickets(ticketsToRelease);

            // Log the vendor activity
            if (added) {
                synchronized (this) {
                    remainingTickets -= ticketsToRelease;  // Decrease the number of tickets left for this vendor
                    System.out.println("Vendor " + vendorId + " released " + ticketsToRelease + " tickets. " +
                            "Remaining tickets: " + remainingTickets  + ".");
                }
            }

            try {
                // Sleep for the specified release interval before releasing more tickets
                Thread.sleep(releaseInterval*1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;  // Exit the loop if interrupted
            }
        }

        // Once the vendor thread exits, log its termination
        System.out.println("Vendor " + vendorId + " is no longer active.");
    }
}
