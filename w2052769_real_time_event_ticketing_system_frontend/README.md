# Ticketing System

A web-based ticketing system that allows for configuration, system control (start/stop), viewing logs, and sales report management. This project leverages WebSockets for real-time updates and Axios for API requests.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [API Integration](#api-integration)
- [WebSocket Integration](#websocket-integration)

## Installation

Follow these steps to get the project up and running on your local machine:

### Prerequisites

Make sure you have the following installed:

- **Node.js**: [Download Node.js](https://nodejs.org/)
- **npm**: Node package manager comes bundled with Node.js.
- **Backend API**: This system assumes that the backend API is running at `http://localhost:8080`. Ensure the API is set up and running before starting the frontend.

### Steps

1. Clone this repository:
    ```bash
    git clone https://github.com/your-repo/ticketing-system.git
    ```

2. Navigate to the project directory:
    ```bash
    cd ticketing-system
    ```

3. Install the dependencies:
    ```bash
    npm install
    ```

4. Create a `.env` file in the root directory and configure your backend URL if necessary:
    ```bash
    REACT_APP_API_URL=http://localhost:8080/api/ticket-system
    ```

5. Start the development server:
    ```bash
    npm start
    ```

The app will be running at `http://localhost:3000` by default.

---

## Usage

Once the application is running, you can interact with the system through the following features:

### Configuration
- **Load Configuration**: The system will automatically load the configuration data from the backend when it starts.
- **Submit Configuration**: Modify and submit configuration data to the backend using the provided form.

### System Control
- **Start System**: Start the ticketing system, which will begin accepting tickets and generating logs.
- **Stop System**: Stop the system and view the sales report.

### Viewing Logs
- Logs will be streamed in real-time using WebSockets. Errors, system status updates, and ticket information will be logged and displayed.

### Sales Report
- After stopping the system, a sales report will be displayed, showing remaining tickets, customer purchases, and vendor sales.

---

## API Integration

The frontend communicates with the backend through the following API endpoints:

### Endpoints

1. **Get Configuration**
   - **URL**: `GET /api/ticket-system/config/get`
   - **Description**: Fetch the current system configuration.
   
2. **Set Configuration**
   - **URL**: `POST /api/ticket-system/config/set`
   - **Body**: JSON object with configuration data (e.g., `totalTickets`, `maxTicketCapacity`).

3. **Start System**
   - **URL**: `POST /api/ticket-system/system/start`
   - **Description**: Start the ticketing system.

4. **Stop System**
   - **URL**: `POST /api/ticket-system/system/stop`
   - **Description**: Stop the ticketing system and generate a sales report.

5. **Get Remaining Tickets**
   - **URL**: `GET /api/ticket-system/system/getremaingticket`
   - **Description**: Fetch the remaining number of tickets.

6. **Get Recent Logs**
   - **URL**: `GET /api/ticket-system/recent`
   - **Description**: Fetch recent logs.

---

## WebSocket Integration

The system uses WebSockets to stream logs and system updates in real-time.

### WebSocket Connection

- **WebSocket URL**: `ws://localhost:8080/logs`
- **Events**:
  - **`onmessage`**: When a new log message is received, it is added to the logs.
  - **`onerror`**: If an error occurs in the WebSocket connection, an error message is displayed.
  - **`onclose`**: Logs a message when the WebSocket connection is closed.

---

## Notes

- Ensure that your backend API is properly configured and running at `localhost:8080` for the frontend to communicate with it.
- You can configure WebSocket and API URLs in `.env` if needed for different environments (e.g., production).
