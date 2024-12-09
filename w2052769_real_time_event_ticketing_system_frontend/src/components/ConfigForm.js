import React, { useState } from 'react';
import { loadConfigFromAPI, postConfigToAPI } from '../services/api';  // API service

const ConfigForm = ({ onSubmit }) => {
    const [config, setConfig] = useState({
        maxTicketCapacity: 1,
        ticketsPerRelease: 1,
        ticketReleaseInterval: 1,
        customerRetrievalInterval: 1,
        vendorCount: 1,
        customerCount: 1,
    });

    const [loading, setLoading] = useState(false);  // Track the loading state
    const [isSubmitting, setIsSubmitting] = useState(false);  // Track the submit button state

    // Function to load the configuration
    const loadConfig = async () => {
        setLoading(true);  // Set loading state to true when starting the load
        try {
            const apiConfig = await loadConfigFromAPI();  // Fetch the configuration from API
            setConfig(apiConfig);  // Set the configuration to the state
        } catch (error) {
            console.error('Error loading configuration from API:', error);
        } finally {
            setLoading(false);  // Reset loading state once the API call is completed
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        
        // Ensure that only positive numbers are entered
        if (value >= 0) {
            setConfig((prevConfig) => ({
                ...prevConfig,
                [name]: value,
            }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);  // Disable the submit button until response is received
        try {
            // Post the config to the API
            const response = await postConfigToAPI(config);
            console.log('Config posted to API:', response);
            onSubmit(config);  // Call onSubmit function passed from the parent
        } catch (error) {
            console.error('Error posting config:', error);
        } finally {
            setIsSubmitting(false);  // Re-enable the submit button after posting
        }
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4 max-w-lg w-full bg-white p-6 shadow-lg rounded-lg">
            <div className="flex flex-col">
                <label>Max Ticket Capacity</label>
                <input
                    type="number"
                    name="maxTicketCapacity"
                    value={config.maxTicketCapacity}
                    onChange={handleChange}
                    min="1"
                    className="px-4 py-2 border border-gray-300 rounded-md"
                    disabled={loading}  // Disable input while loading
                    required
                />
            </div>

            <div className="flex flex-col">
                <label>Tickets Per Release</label>
                <input
                    type="number"
                    name="ticketsPerRelease"
                    value={config.ticketsPerRelease}
                    onChange={handleChange}
                    min="1"
                    className="px-4 py-2 border border-gray-300 rounded-md"
                    disabled={loading}  // Disable input while loading
                    required
                />
            </div>

            <div className="flex flex-col">
                <label>Ticket Release Interval (seconds)</label>
                <input
                    type="number"
                    name="ticketReleaseInterval"
                    value={config.ticketReleaseInterval}
                    onChange={handleChange}
                    min="1"
                    className="px-4 py-2 border border-gray-300 rounded-md"
                    disabled={loading}  // Disable input while loading
                    required
                />
            </div>

            <div className="flex flex-col">
                <label>Customer Retrieval Interval (seconds)</label>
                <input
                    type="number"
                    name="customerRetrievalInterval"
                    value={config.customerRetrievalInterval}
                    onChange={handleChange}
                    min="1"
                    className="px-4 py-2 border border-gray-300 rounded-md"
                    disabled={loading}  // Disable input while loading
                    required
                />
            </div>

            <div className="flex flex-col">
                <label>Vendor Count</label>
                <input
                    type="number"
                    name="vendorCount"
                    value={config.vendorCount}
                    onChange={handleChange}
                    min="1"
                    className="px-4 py-2 border border-gray-300 rounded-md"
                    disabled={loading}  // Disable input while loading
                    required
                />
            </div>

            <div className="flex flex-col">
                <label>Customer Count</label>
                <input
                    type="number"
                    name="customerCount"
                    value={config.customerCount}
                    onChange={handleChange}
                    min="1"
                    className="px-4 py-2 border border-gray-300 rounded-md"
                    disabled={loading}  // Disable input while loading
                    required
                />
            </div>

            <div className="flex space-x-4 justify-center">
                {/* Load Configuration button */}
                <button
                    type="button"
                    onClick={loadConfig}
                    className={`w-[200px] px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 ${loading ? 'cursor-not-allowed bg-gray-400' : ''}`}
                    disabled={isSubmitting || loading}  // Disable if submitting or loading is true
                >
                    {loading ? 'Loading...' : 'Load Config'}
                </button>

                {/* Submit button */}
                <button
                    type="submit"
                    className={`w-[200px] px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 ${isSubmitting || loading ? 'cursor-not-allowed bg-gray-400' : ''}`}
                    disabled={isSubmitting || loading}  // Disable if submitting or loading is true
                >
                    {isSubmitting ? 'Submitting...' : 'Submit Configuration'}
                </button>
            </div>
        </form>
    );
};

export default ConfigForm;
