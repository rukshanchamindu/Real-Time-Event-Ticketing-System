// src/components/Popup.js
import React from 'react';

const Popup = ({ type, message, onClose }) => {
    const getBackgroundColor = () => {
        switch (type) {
            case 'loading':
                return 'bg-blue-500';  // Blue for loading
            case 'error':
                return 'bg-red-500';   // Red for error
            case 'info':
                return 'bg-green-500'; // Green for success/info
            default:
                return 'bg-gray-500';  // Default gray for any undefined type
        }
    };

    const getIcon = () => {
        switch (type) {
            case 'loading':
                return '🔄';  // Loading spinner emoji
            case 'error':
                return '❌';  // Error cross emoji
            case 'info':
                return '✔️';  // Success checkmark emoji
            default:
                return 'ℹ️';  // Info emoji
        }
    };

    return (
        <div className={`fixed top-4 left-1/2 transform -translate-x-1/2 p-4 rounded-md shadow-lg ${getBackgroundColor()} text-white max-w-md w-full`}>
            <div className="flex items-center space-x-2">
                <span>{getIcon()}</span>
                <p className="flex-1">{message}</p>
                <button onClick={onClose} className="ml-4 p-2 bg-white text-gray-700 rounded-md hover:bg-gray-200">
                    ✖️
                </button>
            </div>
        </div>
    );
};

export default Popup;
