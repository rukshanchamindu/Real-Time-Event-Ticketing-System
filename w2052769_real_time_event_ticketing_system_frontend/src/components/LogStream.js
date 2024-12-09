import React, { useEffect, useRef } from 'react';

const LogStream = ({ logs }) => {
    const logEndRef = useRef(null);  // Reference to the bottom of the log container

    // Effect to scroll to the bottom whenever logs change
    useEffect(() => {
        if (logEndRef.current) {
            logEndRef.current.scrollIntoView({ behavior: 'smooth' });  // Smooth scroll to the bottom
        }
    }, [logs]);  // Dependency on logs, so it runs every time logs change

    return (
        <div className="w-1/2 bg-gray-100 rounded-lg flex flex-col items-start space-y-2">
            {/* Log Stream Title outside the log container */}
            <div className="text-2xl font-semibold">Live Log Stream</div>

            {/* Log Container */}
            <div className="bg-white shadow-lg rounded-lg p-4 overflow-y-auto" style={{ maxHeight: '50vh', width: '100%' }}>
                <div className="space-y-2">
                    {logs.map((log, index) => (
                        <div key={index} className="text-gray-700">
                            {log}
                        </div>
                    ))}
                    <div ref={logEndRef} />  {/* Empty div used to trigger scroll to bottom */}
                </div>
            </div>
        </div>
    );
};

export default LogStream;
