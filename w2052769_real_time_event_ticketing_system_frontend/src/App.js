import React, { useState, useEffect } from 'react';
import ConfigForm from './components/ConfigForm';  // Import the form component
import SplashScreen from './components/SplashScreen';  // Import the splash screen component
import { postConfigToAPI, fetchLogsFromAPI, startSystemAPI, stopSystemAPI } from './services/api'; 
import LogStream from './components/LogStream';  // Import the LogStream component
import ConfigurationDetails from './components/ConfigurationDetails';  // Import the ConfigurationDetails component
import Popup from './components/Popup';  // Import Popup component
import Buttons from './components/Buttons';  // Import the Buttons component

const App = () => {
    const [showSplash, setShowSplash] = useState(true);
    const [configData, setConfigData] = useState(null);
    const [isSystemStarted, setIsSystemStarted] = useState(false);
    const [logs, setLogs] = useState([]);
    const [popup, setPopup] = useState({ isOpen: false, type: '', message: '' });
    const [loading, setLoading] = useState(false);  // Track loading state

    const handleConfigSubmit = async (data) => {
        setConfigData(data);
        console.log('Form Submitted:', data);

        try {
            const response = await postConfigToAPI(data);
            console.log('Config posted to API:', response);
        } catch (error) {
            showPopup('error', 'Error posting config');
        }
    };

    const simulateLogUpdates = async () => {
        try {
            const newLogs = await fetchLogsFromAPI();  // Fetch new logs from API
            setLogs((prevLogs) => [...prevLogs, ...newLogs]);
        } catch (error) {
            showPopup('error', 'Error fetching logs');
        }
    };

    const handleStartSystem = async () => {
        showPopup('loading', 'Starting system...');  // Show loading popup
        setLoading(true);

        try {
            const response = await startSystemAPI();  // Call API to start the system
            if (response) {
                setIsSystemStarted(true);
                showPopup('info', 'System Started Successfully');
                setInterval(simulateLogUpdates, 1000);  // Start fetching logs
            }
        } catch (error) {
            showPopup('error', 'Error starting system');
        } finally {
            setLoading(false);
        }
    };

    const handleStopSystem = async () => {
        showPopup('loading', 'Stopping system...');  // Show loading popup
        setLoading(true);

        try {
            const response = await stopSystemAPI();  // Call API to stop the system
            if (response) {
                setIsSystemStarted(false);
                showPopup('info', 'System Stopped');
            }
        } catch (error) {
            showPopup('error', 'Error stopping system');
        } finally {
            setLoading(false);
        }
    };

    const showPopup = (type, message) => {
        setPopup({ isOpen: true, type, message });
        setTimeout(() => {
            setPopup({ isOpen: false, type: '', message: '' });  // Close the popup after 3 seconds
        }, 3000);
    };

    const handleSplashEnd = () => {
        setShowSplash(false);
    };

    // Handle page unload (tab close / refresh)
    useEffect(() => {
        const handleBeforeUnload = (event) => {
            if (isSystemStarted) {
                stopSystemAPI(); // Stop the system when the page is being unloaded
            }
            return undefined;  // Necessary to ensure the unload event works properly
        };

        // Attach the unload event listener
        window.addEventListener('beforeunload', handleBeforeUnload);

        // Cleanup the event listener when the component is unmounted
        return () => {
            window.removeEventListener('beforeunload', handleBeforeUnload);
        };
    }, [isSystemStarted]);

    return (
        <div className="App">
            {popup.isOpen && (
                <Popup type={popup.type} message={popup.message} onClose={() => setPopup({ isOpen: false, type: '', message: '' })} />
            )}

            {showSplash ? (
                <SplashScreen onSplashEnd={handleSplashEnd} />
            ) : (
                <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
                    {!configData ? (
                        <ConfigForm onSubmit={handleConfigSubmit} />
                    ) : (
                        <>
                            <ConfigurationDetails configData={configData} />

                            {/* Buttons Component */}
                            <Buttons 
                                isSystemStarted={isSystemStarted} 
                                onStart={handleStartSystem} 
                                onStop={handleStopSystem} 
                                loading={loading} 
                            />

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
