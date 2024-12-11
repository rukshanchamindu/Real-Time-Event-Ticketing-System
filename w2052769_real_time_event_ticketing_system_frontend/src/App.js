import React, { useState, useEffect, useRef } from 'react';
import ConfigForm from './components/ConfigForm'; // Import the form component
import SplashScreen from './components/SplashScreen'; // Import the splash screen component
import { postConfigToAPI, startSystemAPI, stopSystemAPI, fetchRemainingTickets } from './services/api';
import LogStream from './components/LogStream'; // Import the LogStream component
import ConfigurationDetails from './components/ConfigurationDetails'; // Import the ConfigurationDetails component
import Popup from './components/Popup'; // Import Popup component
import ControlPanel from './components/ControlPanel'; // Import the ControlPanel component
import SalesReportOverlay from './components/SalesReportOverlay'; // Import the SalesReportOverlay component

const App = () => {
  const [showSplash, setShowSplash] = useState(true);
  const [configData, setConfigData] = useState(null);
  const [isSystemStarted, setIsSystemStarted] = useState(false);
  const [systemStatus, setSystemStatus] = useState('Not Started'); // Track system status
  const [remainingTickets, setRemainingTickets] = useState(0); // Track remaining tickets
  const [totalTickets, setTotalTickets] = useState(0); // Track total tickets
  const [popup, setPopup] = useState({ isOpen: false, type: '', message: '' });
  const [loading, setLoading] = useState(false); // Track loading state
  const [logs, setLogs] = useState([]); // Store logs received via WebSocket
  const [ws, setWs] = useState(null); // Store WebSocket connection
  const [error, setError] = useState(null); // Store WebSocket errors
  const [salesReport, setSalesReport] = useState(''); // Store sales report
  const [showSalesReport, setShowSalesReport] = useState(false); // Track if sales report should be shown
  const socketRef = useRef(null); // Reference for WebSocket connection

  // Establish WebSocket connection with retry logic
  const createWebSocket = () => {
    const socket = new WebSocket('ws://localhost:8080/logs');

    socket.onopen = () => {
      console.log('WebSocket connection established');
      setError(null); // Reset any previous error
    };

    socket.onmessage = (event) => {
      const newLog = event.data;
      setLogs((prevLogs) => [...prevLogs, newLog]);
    };

    socket.onerror = (event) => {
      setError('WebSocket error: ' + event.message);
      console.error('WebSocket error:', event);
    };

    socket.onclose = () => {
      console.log('WebSocket connection closed');
    };

    setWs(socket);
    socketRef.current = socket; // Store in ref for cleanup later
  };

  // Close WebSocket connection when the system stops
  const closeWebSocket = () => {
    if (socketRef.current) {
      socketRef.current.close(); // Close WebSocket connection on system stop
    }
  };

  // Handle Config form submission
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

  // Handle Start system
  const handleStartSystem = async () => {
    showPopup('loading', 'Starting system...'); // Show loading popup
    setLoading(true);

    try {
      createWebSocket(); // Create the WebSocket connection
      setTimeout(async () => {
        const response = await startSystemAPI(); // Call API to start the system
        if (response) {
          setIsSystemStarted(true);
          setSystemStatus('Running');
          setTotalTickets(response.totalTickets); // Set total tickets from the response
          showPopup('info', 'System Started Successfully');
        } else {
          showPopup('error', 'Failed to start the system');
          closeWebSocket(); // Close the WebSocket if the system is stopped
        }
      }, 1000); // 1 second delay
    } catch (error) {
      showPopup('error', 'Error starting system');
    } finally {
      setLoading(false);
    }
  };

  // Handle Stop system
  const handleStopSystem = async () => {
    showPopup('loading', 'Stopping system...'); // Show loading popup
    setLoading(true);

    try {
      const response = await stopSystemAPI(); // Call API to stop the system
      if (response) {
        setIsSystemStarted(false);
        setSystemStatus('Stopped');
        showPopup('info', 'System Stopped');

        setTimeout(() => {
          closeWebSocket(); // Close WebSocket connection after 1 second
        }, 1000); // 1 second delay

        if (response) {
          setSalesReport(response);
          setShowSalesReport(true); // Show sales report when system stops
        }
      } else {
        showPopup('error', 'Failed to stop the system');
      }
    } catch (error) {
      showPopup('error', 'Error stopping system');
    } finally {
      setLoading(false);
    }
  };

  // Fetch remaining tickets
  const fetchRemainingTicketsHandler = async () => {
    try {
      const response = await fetchRemainingTickets();
      setRemainingTickets(response.remainingTickets);
    } catch (error) {
      console.error('Error fetching remaining tickets', error);
    }
  };

  // Call fetchRemainingTickets every 5 seconds when system is running
  useEffect(() => {
    let intervalId;

    if (isSystemStarted) {
      fetchRemainingTicketsHandler(); // Initial fetch
      intervalId = setInterval(fetchRemainingTicketsHandler, 5000); // Update remaining tickets every 5 seconds

      // Cleanup on component unmount or when system stops
      return () => clearInterval(intervalId);
    } else {
      setRemainingTickets(0); // Reset remaining tickets when system is stopped
    }
  }, [isSystemStarted]);

  const showPopup = (type, message) => {
    setPopup({ isOpen: true, type, message });
    setTimeout(() => {
      setPopup({ isOpen: false, type: '', message: '' }); // Close the popup after 3 seconds
    }, 3000);
  };

  const handleSplashEnd = () => {
    setShowSplash(false);
  };

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
              <div className="flex w-full">
                <div className="flex-1 p-4 flex flex-col items-center justify-center">
                  <ConfigurationDetails configData={configData} />
                  <ControlPanel
                    isSystemStarted={isSystemStarted}
                    onStart={handleStartSystem}
                    onStop={handleStopSystem}
                    loading={loading}
                    systemStatus={systemStatus}
                    remainingTickets={remainingTickets}
                    totalTickets={configData.totalTickets}
                    handleViewSalesReport={() => setShowSalesReport(true)}
                  />
                </div>

                <div className="flex-1 p-4 flex flex-col items-center justify-center">
                  <LogStream logs={logs} error={error} />
                </div>
              </div>
            </>
          )}
        </div>
      )}

      {showSalesReport && (
        <SalesReportOverlay
          salesReport={salesReport}
          onClose={() => setShowSalesReport(false)}
        />
      )}
    </div>
  );
};

export default App;
