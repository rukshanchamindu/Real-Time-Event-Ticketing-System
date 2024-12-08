// src/components/ConfigForm.js
import React, { useState } from 'react';

const ConfigForm = ({ onSubmit }) => {
    const [maxTicketCapacity, setMaxTicketCapacity] = useState('');
    const [ticketsPerRelease, setTicketsPerRelease] = useState('');
    const [ticketReleaseInterval, setTicketReleaseInterval] = useState('');
    const [customerRetrievalInterval, setCustomerRetrievalInterval] = useState('');
    const [vendorCount, setVendorCount] = useState('');
    const [customerCount, setCustomerCount] = useState('');
    const [debug, setDebug] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        const configData = {
            maxTicketCapacity,
            ticketsPerRelease,
            ticketReleaseInterval,
            customerRetrievalInterval,
            vendorCount,
            customerCount,
            debug,
        };
        onSubmit(configData); // Pass data to parent component
    };

    return (
        <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4 bg-white shadow-lg rounded-lg space-y-6">
            <h2 className="text-2xl font-semibold text-center">Ticketing System Configuration</h2>

            <div className="space-y-4">
                <div>
                    <label className="block text-gray-700">Max Ticket Capacity</label>
                    <input
                        type="number"
                        value={maxTicketCapacity}
                        onChange={(e) => setMaxTicketCapacity(e.target.value)}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>

                <div>
                    <label className="block text-gray-700">Tickets Per Release</label>
                    <input
                        type="number"
                        value={ticketsPerRelease}
                        onChange={(e) => setTicketsPerRelease(e.target.value)}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>

                <div>
                    <label className="block text-gray-700">Ticket Release Interval (in seconds)</label>
                    <input
                        type="number"
                        value={ticketReleaseInterval}
                        onChange={(e) => setTicketReleaseInterval(e.target.value)}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>

                <div>
                    <label className="block text-gray-700">Customer Retrieval Interval (in seconds)</label>
                    <input
                        type="number"
                        value={customerRetrievalInterval}
                        onChange={(e) => setCustomerRetrievalInterval(e.target.value)}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>

                <div>
                    <label className="block text-gray-700">Vendor Count</label>
                    <input
                        type="number"
                        value={vendorCount}
                        onChange={(e) => setVendorCount(e.target.value)}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>

                <div>
                    <label className="block text-gray-700">Customer Count</label>
                    <input
                        type="number"
                        value={customerCount}
                        onChange={(e) => setCustomerCount(e.target.value)}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>

                <div className="flex items-center">
                    <input
                        type="checkbox"
                        checked={debug}
                        onChange={(e) => setDebug(e.target.checked)}
                        id="debug"
                        className="mr-2"
                    />
                    <label htmlFor="debug" className="text-gray-700">Enable Debugging</label>
                </div>
            </div>

            <button type="submit" className="w-full bg-blue-500 text-white p-3 rounded-md hover:bg-blue-600 transition">
                Submit Configuration
            </button>
        </form>
    );
};

export default ConfigForm;
