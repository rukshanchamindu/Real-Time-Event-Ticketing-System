import React, { useEffect, useRef } from 'react';

const LogStream = ({ logs, error }) => {
  const logEndRef = useRef(null); // Reference to the bottom of the log container

  // Effect to scroll to the bottom whenever logs change
  useEffect(() => {
    if (logEndRef.current) {
      logEndRef.current.scrollIntoView({ behavior: 'smooth' }); // Smooth scroll to the bottom
    }
  }, [logs]); // Dependency on logs, so it runs every time logs change

  return (
    <div className="w-full h-[75vh] bg-gray-100 flex flex-col items-center justify-start space-y-4 p-4">
      <div className="text-2xl font-semibold">Live Log Stream</div>

      {/* Show error message if WebSocket has error */}
      {error && (
        <div className="text-red-500 bg-red-100 p-2 rounded-md mb-4">
          <strong>Error:</strong> {error}
        </div>
      )}

      {/* Log Container */}
      <div className="bg-white shadow-lg rounded-lg p-4 overflow-y-auto w-full h-full">
        <div className="space-y-2">
          {logs.map((log, index) => (
            <div key={index} className="text-gray-700">
              {log}
            </div>
          ))}
          <div ref={logEndRef} /> {/* Empty div to trigger scroll */}
        </div>
      </div>
    </div>
  );
};

export default LogStream;
