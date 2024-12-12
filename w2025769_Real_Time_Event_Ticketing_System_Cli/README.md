
# Real-Time Event Ticketing System

A Java-based multi-threaded application designed for managing event ticket sales with vendors and customers operating concurrently. The system features a command-line interface (CLI) for configuration, control, and monitoring.

## Features
- Configurable ticket pool with adjustable ticket count and release intervals.
- Vendors and customers operating concurrently using threads.
- Logging of all system activities to track ticket releases and purchases.
- CLI interface for configuration, system control (start/stop), and status updates.

## Setup Instructions

### Prerequisites

Ensure you have the following installed on your system:
- Java Development Kit (JDK) version 8 or higher.
- A text editor or IDE for Java (e.g., IntelliJ IDEA, Eclipse, or VS Code).
- Apache Maven (for dependency management, if needed).

### Steps to Setup

1. **Clone the Repository**

    ```bash
    git clone <repository_url>
    cd Real-Time-Event-Ticketing-System
    ```

2. **Build the Project**

   If you're using Maven:

    ```bash
    mvn clean compile
    ```

   If you're using an IDE, simply open the project and build it.

3. **Run the Application**

   Navigate to the `src` directory and run the `TicketSystemCLI` class:

    ```bash
    cd src
    java cli.TicketSystemCLI
    ```

## Usage Instructions

### Start the System

1. Run the application as described above.
2. On startup, you will see a CLI menu for configuring or loading the system:

    ```bash
    Welcome to the Ticketing System!
    =================================
    1. Load existing configuration
    2. Create new configuration
    ```

### Create or Load a Configuration

- **Option 1**: Load an existing `config.json` file.
- **Option 2**: Create a new configuration by providing the following details:

    - Total tickets
    - Tickets per release
    - Ticket release interval (in seconds)
    - Customer retrieval interval (in seconds)
    - Max ticket pool capacity
    - Number of vendors
    - Number of customers

  **Example Configuration Input:**

    ```plaintext
    Total Ticket Count: 100
    Max Ticket Pool Capacity: 50
    Ticket Release Interval (in seconds): 5
    Tickets Per Release: 10
    Customer Retrieval Interval (in seconds): 2
    Vendor Count: 2
    Customer Count: 5
    ```

### Control the System

Once the configuration is set, you will be prompted to start or stop the system:

```plaintext
1. Start the system
2. Stop the system
3. Exit the system
```

### Monitor System Behavior

- **Vendors** release tickets into the pool at regular intervals.
- **Customers** retrieve tickets from the pool concurrently.
- Logs are generated in `event_ticketing_system.log` for every activity.

### Exit the System

To terminate the system, choose option **3** from the main menu.

---

## Directory Structure

```plaintext
Real-Time-Event-Ticketing-System/
├── src/
│   ├── cli/
│   │   └── TicketSystemCLI.java         # Main CLI entry point
│   ├── config/
│   │   └── Configuration.java          # Handles configuration loading/saving
│   ├── core/
│   │   ├── Ticket.java                 # Represents a ticket
│   │   └── TicketPool.java             # Manages the pool of tickets
│   ├── logger/
│   │   └── LoggerService.java          # Logs system activities
│   ├── threads/
│       ├── Customer.java               # Simulates customers
│       └── Vendor.java                 # Simulates vendors
├── config.json                         # Default configuration file
├── event_ticketing_system.log          # Log file for system activities
└── README.md                           # Setup and usage instructions
```

---

## Key Components

### 1. **TicketPool**
Manages the ticket pool and ensures thread-safe operations for adding and removing tickets.

### 2. **Vendor**
Simulates vendors releasing tickets into the pool at regular intervals.

### 3. **Customer**
Simulates customers retrieving tickets from the pool.

### 4. **LoggerService**
Logs all activities (e.g., ticket releases, customer purchases, interruptions) to a file.

---

## Example Logs

The system generates logs in the `event_ticketing_system.log`. Example:

```plaintext
10/12/24 - 10:45 - Vendor-1 released 10 tickets. Remaining tickets: 40
10/12/24 - 10:46 - Customer-1 purchased: Ticket ID: 1
10/12/24 - 10:47 - Vendor-2 released 5 tickets. Remaining tickets: 35
```

