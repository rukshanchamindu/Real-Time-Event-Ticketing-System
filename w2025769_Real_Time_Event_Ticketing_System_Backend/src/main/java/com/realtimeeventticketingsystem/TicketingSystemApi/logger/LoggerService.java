package com.realtimeeventticketingsystem.TicketingSystemApi.logger;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoggerService {

    private static List<String> logBuffer = new ArrayList<>();  // Original buffer for writing to file
    private static List<String> memoryLogs = new ArrayList<>();  // In-memory log storage (last 100 logs)
    private static final int MAX_MEMORY_LOGS = 500;  // Limit for in-memory logs
    private static final String LOG_FILE_PATH = "event_ticketing_system.log";  // Log file path
    private static BufferedWriter writer;  // BufferedWriter for writing logs to file
    private static File logFile = new File(LOG_FILE_PATH);

    // Set to store active WebSocket sessions
    private static Set<WebSocketSession> activeSessions = new HashSet<>();

    static {
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(logFile, true));

            // Start a separate thread to flush logs every 1 second
            Thread logFlusher = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);  // Wait for 1 second
                        flushLogs();         // Write logs to file every 1 second
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            logFlusher.setDaemon(true);  // Daemon thread will stop when the main application exits
            logFlusher.start();
        } catch (IOException e) {
            System.err.println("Error initializing the log file: " + e.getMessage());
        }
    }

    // Method to log a message (store in memory and buffer)
    public static void log(String message) {
        String logMessage = String.format("%s - %s%n", getFormattedTimestamp(), message);

        // Store log in the buffer for writing to the file
        logBuffer.add(logMessage);  // Add to the buffer to be written to the file later

        // Broadcast the message to all WebSocket sessions
        broadcastLogs(logMessage);
    }

    // Method to flush logs to the file every 1 second and add to memory
    private static synchronized void flushLogs() {
        if (!logBuffer.isEmpty()) {
            try {
                // Write all the logs in the buffer to the file
                for (String logMessage : logBuffer) {
                    writer.write(logMessage);  // Write to the log file
                }
                writer.flush();  // Ensure everything is written to the file

                // Add logs to memory logs (avoid duplication)
                for (String logMessage : logBuffer) {
                    if (!memoryLogs.contains(logMessage)) {
                        // Keep only the last 100 logs in memory
                        if (memoryLogs.size() >= MAX_MEMORY_LOGS) {
                            memoryLogs.remove(0);  // Remove the oldest log if the limit is exceeded
                        }
                        memoryLogs.add(logMessage);  // Add new log to memory
                    }
                }
                logBuffer.clear();  // Clear the buffer after writing
            } catch (IOException e) {
                System.err.println("Failed to write logs to the file: " + e.getMessage());
            }
        }
    }

    // Method to get the formatted current timestamp
    private static String getFormattedTimestamp() {
        return new java.text.SimpleDateFormat("dd/MM/yy - HH:mm").format(new java.util.Date());
    }

    // Method to wipe the log file contents on the first run (clear the file without deleting it)
    public static synchronized void wipeLogFile() {
        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write("");  // Clears the file content (truncate the file)
        } catch (IOException e) {
            System.err.println("Error wiping log file: " + e.getMessage());
        }
    }

    // Method to retrieve the last 100 logs stored in memory
    public static List<String> getRecentLogs() {
        return new ArrayList<>(memoryLogs);  // Return a copy of the recent logs
    }

    // Add a WebSocket session to the active sessions set
    public static void addSession(WebSocketSession session) {
        activeSessions.add(session);
        System.out.println("WebSocket session added: " + session.getId());
    }

    // Remove a WebSocket session from the active sessions set
    public static void removeSession(WebSocketSession session) {
        activeSessions.remove(session);
        System.out.println("WebSocket session removed: " + session.getId());
    }

    // Broadcast logs to all active WebSocket sessions
    // Broadcast logs to all active WebSocket sessions
    public static synchronized void broadcastLogs(String logMessage) {
        for (WebSocketSession session : activeSessions) {
            try {
                if (session.isOpen()) {
                    // Ensure only one message is sent at a time
                    session.sendMessage(new TextMessage(logMessage));  // Send log message to client
                }
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
                // Handle session failure or closed connections
            }
        }
    }
    public static synchronized void wipeAllSessions() {
        // Clear the activeSessions
        activeSessions.clear();
        System.out.println("All WebSocket sessions wiped.");
    }
}
