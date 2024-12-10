package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class LoggerService {

    private static List<String> logBuffer = new ArrayList<>();  // In-memory log storage
    private static final String LOG_FILE_PATH = "event_ticketing_system.log";  // Log file path
    private static BufferedWriter writer;  // BufferedWriter for writing logs to file
    private static File logFile = new File(LOG_FILE_PATH);

    static {
        // Initialize the BufferedWriter
        try {

            if (!logFile.exists()) {
                // Create the file if it doesn't exist
                logFile.createNewFile();
            } else {
                // Clear the contents of the file on the first run
                wipeLogFile();
            }

            // Open the file in append mode
            writer = new BufferedWriter(new FileWriter(logFile, true));

            // Start a separate thread to flush logs every 1 second
            Thread logFlusher = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);  // Wait for 1 second
                        flushLogs();         // Write logs to file every second
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

    // Method to initialize and return a Logger for a given class
    public static void log(String message) {
        // Format the log message (custom format can be applied)
        String logMessage = String.format("%s - %s%n", getFormattedTimestamp(), message);
        logBuffer.add(logMessage);  // Store log in memory
    }

    // Method to flush logs to the file every second
    private static synchronized void flushLogs() {
        if (!logBuffer.isEmpty()) {
            try {
                // Write all the logs in the buffer to the file
                for (String logMessage : logBuffer) {
                    writer.write(logMessage);  // Write to the log file
                }
                writer.flush();  // Ensure everything is written to the file
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
    private static synchronized void wipeLogFile() {
        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write("");  // Clears the file content (truncate the file)
        } catch (IOException e) {
            System.err.println("Error wiping log file: " + e.getMessage());
        }
    }
}
