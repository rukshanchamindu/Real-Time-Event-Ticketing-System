// src/components/SplashScreen.js
import React, { useEffect } from 'react';

const SplashScreen = ({ onSplashEnd }) => {
    useEffect(() => {
        const timer = setTimeout(() => {
            onSplashEnd();  // Hide splash screen after 3 seconds
        }, 1000);
        return () => clearTimeout(timer);  // Clean up the timer when the component unmounts
    }, [onSplashEnd]);

    return (
        <div className="w-full h-screen flex items-center justify-center bg-blue-600 text-white">
            <div className="animate-pulse text-4xl font-extrabold">
                Welcome to the Ticketing System!
            </div>
        </div>
    );
};

export default SplashScreen;
