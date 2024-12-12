// src/services/api.js

import axios from 'axios';

// Base URL for the API (assuming backend is running on localhost:8080)
const API_BASE_URL = 'http://localhost:8080/api/ticket-system';

// Fetch the configuration from the backend
export const loadConfigFromAPI = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/config/get`);
        return response.data;  // Return the configuration data
    } catch (error) {
        console.error('Error loading configuration:', error);
        throw error;  // Rethrow the error for further handling
    }
};

// Post the configuration data to the backend
export const postConfigToAPI = async (configData) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/config/set`, configData, {
            headers: {
                'Content-Type': 'application/json',
            },
        });
        return response.data;  // Return the response message or data
    } catch (error) {
        console.error('Error posting configuration:', error);
        throw error;  // Rethrow the error for further handling
    }
};

// Fetch recent logs from the backend
export const fetchLogsFromAPI = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/recent`);
        return response.data;  // Return the logs from the backend
    } catch (error) {
        console.error('Error fetching logs:', error);
        throw error;  // Rethrow the error for further handling
    }
};

// Start the system by making a POST request to the backend
export const startSystemAPI = async () => {
    try {
        const response = await axios.post(`${API_BASE_URL}/system/start`);
        return response.data;  // Return the success message
    } catch (error) {
        console.error('Error starting the system:', error);
        throw error;  // Rethrow the error for further handling
    }
};

// Stop the system by making a POST request to the backend
export const stopSystemAPI = async () => {
    try {
        const response = await axios.post(`${API_BASE_URL}/system/stop`);
        return response.data;  // Return the success message
    } catch (error) {
        console.error('Error stopping the system:', error);
        throw error;  // Rethrow the error for further handling
    }
};

export const fetchRemainingTickets = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/system/getremaingticket`);
        return response.data;  // Assuming the response is in the form { remainingTickets: number }
    } catch (error) {
        console.error('Error fetching remaining tickets:', error);
        throw error;
    }
};