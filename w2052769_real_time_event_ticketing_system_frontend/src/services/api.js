// src/services/api.js

// Simulated function to load configuration from API (already existing)
export const loadConfigFromAPI = async () => {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({
                maxTicketCapacity: 500,
                ticketsPerRelease: 10,
                ticketReleaseInterval: 5,
                customerRetrievalInterval: 2,
                vendorCount: 3,
                customerCount: 10,
            });
        }, 2000);  // Simulate a 2-second delay for loading the config
    });
};

// Simulated function to post configuration to an API
export const postConfigToAPI = async (configData) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            // Simulate a successful API response after posting the data
            console.log("Posting config to API:", configData);
            resolve({ status: 'success', data: configData });  // Simulate a successful response
        }, 2000);  // Simulate a 2-second delay for posting the data
    });
};

// Simulated function to fetch logs from an API
export const fetchLogsFromAPI = async () => {
    const logs = [
        "System started successfully.",
        "Configuration loaded from API.",
        "Vendor 1 added 10 tickets.",
        "Customer 1 purchased a ticket.",
        "System is running smoothly.",
    ];

    // Simulate getting new logs
    return new Promise((resolve) => {
        setTimeout(() => {
            const randomLog = logs[Math.floor(Math.random() * logs.length)];
            resolve([randomLog]);  // Return one random log message at a time
        }, 1000);  // Simulate a 1-second delay for fetching logs
    });
};