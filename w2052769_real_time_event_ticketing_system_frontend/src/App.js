import React, { useState } from 'react';
import ConfigForm from './components/ConfigForm';  // Import the form component
import SplashScreen from './components/SplashScreen';  // Import the splash screen component
import { postConfigToAPI, fetchLogsFromAPI } from './services/api';  // Import API service
import LogStream from './components/LogStream';  // Import the LogStream component

const App = () => {
    const [showSplash, setShowSplash] = useState(true);  // State to control splash screen visibility
    const [configData, setConfigData] = useState(null);  // State to store the configuration data
    const [isSystemStarted, setIsSystemStarted] = useState(false);  // Track if the system is started
    const [logs, setLogs] = useState([]);  // State to store logs

    // Handle configuration submission
    const handleConfigSubmit = async (data) => {
        setConfigData(data);
        console.log('Form Submitted:', data);

        // Post the config to the API
        try {
            const response = await postConfigToAPI(data);
            console.log('Config posted to API:', response);
        } catch (error) {
            console.error('Error posting config:', error);
        }
    };

    // Fetch logs every 1 second (sped up log fetching)
    const simulateLogUpdates = async () => {
        try {
            const newLogs = await fetchLogsFromAPI();  // Get logs from the simulated API
            setLogs((prevLogs) => [...prevLogs, ...newLogs]);
        } catch (error) {
            console.error('Error fetching logs:', error);
        }
    };

    // Handle system start
    const handleStartSystem = () => {
        setIsSystemStarted(true);
        console.log('System Started');
        // Start fetching logs
        setInterval(simulateLogUpdates, 1000);  // Fetch logs every 1 second
    };

    // Handle system stop
    const handleStopSystem = () => {
        setIsSystemStarted(false);
        console.log('System Stopped');
    };

    // Handle splash screen end
    const handleSplashEnd = () => {
        setShowSplash(false);  // Hide the splash screen after 3 seconds
    };

    return (
        <div className="App">
            {showSplash ? (
                <SplashScreen onSplashEnd={handleSplashEnd} />  // Show splash screen
            ) : (
                <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
                    {!configData ? (
                        <ConfigForm onSubmit={handleConfigSubmit} />
                    ) : (
                        <>
                            <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
                                <h2 className="text-xl font-bold mb-4">Configuration Details</h2>
                                <div>
                                    <p><strong>Max Ticket Capacity:</strong> {configData.maxTicketCapacity}</p>
                                    <p><strong>Tickets Per Release:</strong> {configData.ticketsPerRelease}</p>
                                    <p><strong>Ticket Release Interval (in seconds):</strong> {configData.ticketReleaseInterval}</p>
                                    <p><strong>Customer Retrieval Interval (in seconds):</strong> {configData.customerRetrievalInterval}</p>
                                    <p><strong>Vendor Count:</strong> {configData.vendorCount}</p>
                                    <p><strong>Customer Count:</strong> {configData.customerCount}</p>
                                </div>
                            </div>

                            <div className="flex space-x-4">
                                <button
                                    onClick={handleStartSystem}
                                    className={`px-4 py-2 ${isSystemStarted ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-600'} text-white rounded-md`}
                                    disabled={isSystemStarted}
                                >
                                    Start System
                                </button>

                                <button
                                    onClick={handleStopSystem}
                                    className={`px-4 py-2 ${!isSystemStarted ? 'bg-gray-400 cursor-not-allowed' : 'bg-red-500 hover:bg-red-600'} text-white rounded-md`}
                                    disabled={!isSystemStarted}
                                >
                                    Stop System
                                </button>
                            </div>

                            {/* Add some space below the buttons */}
                            <div className="my-6"></div>

                            {/* Only show LogStream if system is started */}
                            {isSystemStarted && <LogStream logs={logs} />}
                        </>
                    )}
                </div>
            )}
        </div>
    );
};

export default App;
