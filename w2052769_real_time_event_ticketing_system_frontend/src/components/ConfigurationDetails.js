// src/components/ConfigurationDetails.js
import React from 'react';

const ConfigurationDetails = ({ configData }) => {
    return (
        <div className="flex items-center justify-center h-full bg-gray-100 m-0">
            <div className="bg-white shadow-lg rounded-lg p-6 mb-6 max-w-lg w-full">
                <h2 className="text-xl font-bold mb-4">Configuration Details</h2>
                <div>
                    <p><strong>Total Ticket Count:</strong> {configData.totalTickets}</p>
                    <p><strong>Max Ticket Capacity:</strong> {configData.maxTicketCapacity}</p>
                    <p><strong>Tickets Per Release:</strong> {configData.ticketsPerRelease}</p>
                    <p><strong>Ticket Release Interval (in seconds):</strong> {configData.ticketReleaseInterval}</p>
                    <p><strong>Customer Retrieval Interval (in seconds):</strong> {configData.customerRetrievalInterval}</p>
                    <p><strong>Vendor Count:</strong> {configData.vendorCount}</p>
                    <p><strong>Customer Count:</strong> {configData.customerCount}</p>
                </div>
            </div>
        </div>
    );
};

export default ConfigurationDetails;
