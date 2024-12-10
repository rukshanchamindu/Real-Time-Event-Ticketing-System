// src/components/Buttons.js
import React from 'react';

const Buttons = ({ isSystemStarted, onStart, onStop, loading }) => {
    return (
        <div className="flex space-x-4 mb-6">
            {/* Start Button */}
            <button
                onClick={onStart}
                className={`px-4 py-2 ${isSystemStarted ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-600'} text-white rounded-md`}
                disabled={isSystemStarted || loading}
            >
                {loading ? 'Starting...' : 'Start System'}
            </button>

            {/* Stop Button */}
            <button
                onClick={onStop}
                className={`px-4 py-2 ${!isSystemStarted ? 'bg-gray-400 cursor-not-allowed' : 'bg-red-500 hover:bg-red-600'} text-white rounded-md`}
                disabled={!isSystemStarted || loading}
            >
                {loading ? 'Stopping...' : 'Stop System'}
            </button>
        </div>
    );
};

export default Buttons;
