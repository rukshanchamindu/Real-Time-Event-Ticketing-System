package com.realtimeeventticketingsystem.TicketingSystemApi.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class LoggerService {

    private static List<String> logBuffer = new ArrayList<>();  // Original buffer for writing to file
    private static List<String> memoryLogs = new ArrayList<>();  // In-memory log storage (last 100 logs)
    private static final int MAX_MEMORY_LOGS = 500;  // Limit for in-memory logs
    private static final String LOG_FILE_PATH = "event_ticketing_system.log";  // Log file path
    private static BufferedWriter writer;  // BufferedWriter for writing logs to file
    private static File logFile = new File(LOG_FILE_PATH);

    static {
        try {
            if (!logFile.exists()) {
                // Create the file if it doesn't exist
                logFile.createNewFile();
            }

            // Open the file in append mode
            writer = new BufferedWriter(new FileWriter(logFile, true));

            // Start a separate thread to flush logs every 5 seconds
            Thread logFlusher = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);  // Wait for 5 seconds
                        flushLogs();         // Write logs to file every 5 seconds
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
        // Format the log message (custom format can be applied)
        String logMessage = String.format("%s - %s%n", getFormattedTimestamp(), message);

        // Store log in the buffer for writing to the file
        logBuffer.add(logMessage);  // Add to the buffer to be written to the file later
    }

    // Method to flush logs to the file every 5 seconds and add to memory
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
                    if (!memoryLogs.contains(logMessage)) {  // Avoid adding duplicates
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
}
