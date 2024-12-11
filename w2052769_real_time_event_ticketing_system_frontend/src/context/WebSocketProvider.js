import React, { createContext, useState, useEffect } from 'react';
import useWebSocket from '../hooks/useWebSocket'; // Custom hook for WebSocket handling

export const WebSocketContext = createContext();

const WebSocketProvider = ({ children }) => {
  const [logs, setLogs] = useState([]);
  const [error, setError] = useState(null);

  const socketUrl = 'ws://localhost:8080/logs'; // Replace with actual WebSocket URL

  // Handle incoming messages from WebSocket
  const handleMessage = (event) => {
    // Add new log message to the state
    setLogs((prevLogs) => [...prevLogs, event.data]);
  };

  // Handle WebSocket errors
  const handleError = (event) => {
    setError('WebSocket Error: ' + event.message); // Set error message to state
    console.error('WebSocket error:', event);
  };

  // Handle WebSocket close event
  const handleClose = () => {
    console.log('WebSocket connection closed');
  };

  // Using the custom WebSocket hook to manage connection
  useWebSocket(socketUrl, handleMessage, handleClose, handleError);

  return (
    <WebSocketContext.Provider value={{ logs, error }}>
      {children}
    </WebSocketContext.Provider>
  );
};

export default WebSocketProvider;
