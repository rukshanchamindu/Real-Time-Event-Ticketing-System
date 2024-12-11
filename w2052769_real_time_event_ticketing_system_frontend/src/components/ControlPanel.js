import React, { useState, useEffect } from 'react';
import { fetchRemainingTickets } from '../services/api'; // API call to fetch remaining tickets

const ControlPanel = ({ isSystemStarted, onStart, onStop, loading, systemStatus, totalTickets, handleViewSalesReport }) => {
    const [remainingTicketCount, setRemainingTicketCount] = useState(0);

    // Fetch remaining tickets when system is running
    useEffect(() => {
        let intervalId;

        if (isSystemStarted && remainingTicketCount !== 0 ) {
            // Function to fetch remaining tickets
            const getRemainingTickets = async () => {
                try {
                    const response = await fetchRemainingTickets(); // Call the API
                    if (response !== null ) {
                        setRemainingTicketCount(response);
                    } else {
                        setRemainingTicketCount(null);
                    }
                } catch (error) {
                    console.error("Error fetching remaining tickets", error);
                }
            };

            // Initial fetch
            getRemainingTickets();
            // Fetch remaining tickets every 5 seconds when system is running
            intervalId = setInterval(getRemainingTickets, 5000); // 5 seconds interval

            // Cleanup interval on component unmount or when system stops
            return () => clearInterval(intervalId);
        } else {
            setRemainingTicketCount(null); // Reset remaining tickets when the system is not running
        }
    }, [isSystemStarted]);

    const handleStopClick = () => {
        if (remainingTicketCount === 0) {
            onStop(); // Otherwise, stop the system
            // handleViewSalesReport(); // If no tickets left, show the sales report
            setRemainingTicketCount(null);
        } else {
            onStop(); // Otherwise, stop the system
        }
    };

    return (
        <div className="flex flex-col items-center justify-center mb-6 p-4">
            {/* System Status */}
            <div className="text-xl font-semibold mb-4">
                <span className={`text-${systemStatus === "Running" ? 'green' : systemStatus === "Stopped" ? 'red' : 'gray'}-500`}>
                    System Status: {systemStatus || 'Not Started'}
                </span>
            </div>

            {/* Remaining Tickets (dynamically updated) */}
            {isSystemStarted && remainingTicketCount !== null && remainingTicketCount !== undefined && (
                <div className="mb-4 text-lg">
                    <strong>Remaining Tickets: </strong>
                    {remainingTicketCount} / {totalTickets}
                </div>
            )}

            {/* Start Button */}
            <button
                onClick={onStart}
                className={`px-4 py-2 ${isSystemStarted ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-600'} text-white rounded-md mb-4`}
                disabled={isSystemStarted || loading}
            >
                {loading ? 'Starting...' : 'Start System'}
            </button>

            {/* Stop Button or View Sales Report */}
            <button
                onClick={handleStopClick}
                className={`px-4 py-2 ${(!isSystemStarted || remainingTicketCount == null || loading) ? 'bg-gray-400 cursor-not-allowed' : 'bg-red-500 hover:bg-red-600'} text-white rounded-md`}
                disabled={!isSystemStarted || remainingTicketCount == null || loading}
            >
                {remainingTicketCount === 0 ? 'View Sales Report' : (loading ? 'Stopping...' : 'Stop System')}
            </button>
        </div>
    );
};

export default ControlPanel;
