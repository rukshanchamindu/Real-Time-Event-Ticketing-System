
# Real-Time Event Ticketing System

## Introduction
This project implements a real-time ticketing system that allows customers to buy tickets while vendors can add tickets to the system. The system is designed with a WebSocket connection to broadcast log messages and provide real-time updates to clients. The core components of the system include a `TicketPool` for ticket management, a `LoggerService` for logging system activities, and a WebSocket handler to broadcast log messages to connected clients.

## Table of Contents
1. [Installation](#installation)
2. [Usage](#usage)
3. [Features](#features)
4. [Dependencies](#dependencies)
5. [Configuration](#configuration)
6. [Documentation](#documentation)
7. [Examples](#examples)
8. [Troubleshooting](#troubleshooting)

## Installation

### Prerequisites
- Java 8 or higher
- Maven (or Gradle) for dependency management
- Spring Boot for application framework
- WebSocket-enabled browser for real-time updates

### Steps
1. Clone the repository:
    ```bash
    git clone https://github.com/your-repository/event-ticketing-system.git
    ```

2. Navigate to the project directory:
    ```bash
    cd event-ticketing-system
    ```

3. Install dependencies (if using Maven):
    ```bash
    mvn install
    ```

4. Build the project:
    ```bash
    mvn clean install
    ```

5. Run the application:
    ```bash
    mvn spring-boot:run
    ```

6. Access the system via the API endpoint (`http://localhost:8080`) and the WebSocket endpoint (`ws://localhost:8080/logs` for log updates).

## Usage

### Starting the System
To start the system, send a POST request to the `/api/ticket-system/system/start` endpoint.

```bash
POST http://localhost:8080/api/ticket-system/system/start
```

### Stopping the System
To stop the system, send a POST request to the `/api/ticket-system/system/stop` endpoint.

```bash
POST http://localhost:8080/api/ticket-system/system/stop
```

### Configuration
You can configure the system by updating the `config.json` file or using the `/api/ticket-system/config/set` endpoint to set new configuration values.

```bash
POST http://localhost:8080/api/ticket-system/config/set
Content-Type: application/json
{
  "totalTickets": 1000,
  "ticketsPerRelease": 100,
  "ticketReleaseInterval": 1000,
  "customerRetrievalInterval": 2000,
  "maxTicketCapacity": 500,
  "vendorCount": 5,
  "customerCount": 10,
  "debug": true
}
```

### Retrieving Remaining Tickets
You can retrieve the number of remaining tickets by sending a GET request to the `/api/ticket-system/system/getremaingticket` endpoint.

```bash
GET http://localhost:8080/api/ticket-system/system/getremaingticket
```

## Features
- Real-time ticket management with WebSocket updates.
- Ticket pool with the ability to add and remove tickets dynamically.
- In-memory logging and log broadcasting to connected WebSocket clients.
- Configurable parameters including ticket release rate, maximum ticket capacity, and customer/vendor count.

## Dependencies
- Spring Boot
- Gson (for JSON parsing)
- WebSocket (for real-time communication)
- Maven (for project management)

## Configuration
The system's configuration is stored in a JSON file called `config.json`. Here is a sample configuration:

```json
{
  "totalTickets": 1000,
  "ticketsPerRelease": 100,
  "ticketReleaseInterval": 1000,
  "customerRetrievalInterval": 2000,
  "maxTicketCapacity": 500,
  "vendorCount": 5,
  "customerCount": 10,
  "debug": true
}
```

## Documentation
- **TicketPool**: Manages the pool of available tickets.
- **LoggerService**: Handles logging and broadcasts log messages via WebSocket.
- **LogWebSocketHandler**: Handles WebSocket connections for log broadcasting.
- **TicketSystemController**: Exposes REST endpoints for controlling the system and retrieving information.

## Examples
### Start the system:
```bash
POST http://localhost:8080/api/ticket-system/system/start
```

### Add tickets to the pool:
```bash
POST http://localhost:8080/api/ticket-system/system/start
```

### Stop the system:
```bash
POST http://localhost:8080/api/ticket-system/system/stop
```

## Troubleshooting
- **Logs not showing up in real-time**: Ensure that your WebSocket client is properly connected to the `ws://localhost:8080/logs` endpoint.
- **Tickets not being added**: Check the ticket pool capacity and ensure that vendors are configured correctly.
- **System fails to start**: Verify the configuration file for correctness (e.g., ticket count, release rate, etc.)

