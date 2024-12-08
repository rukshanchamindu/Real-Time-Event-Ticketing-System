// src/App.js
import React, { useState } from 'react';
import ConfigForm from './components/ConfigForm';  // Import the form component
import SplashScreen from './components/SplashScreen';  // Import the splash screen component

const App = () => {
    const [showSplash, setShowSplash] = useState(true);  // State to control splash screen visibility

    const handleConfigSubmit = (configData) => {
        console.log('Form Submitted:', configData);
        // You can send the configData to your backend or process it further
    };

    const handleSplashEnd = () => {
        setShowSplash(false);  // Hide the splash screen after 3 seconds
    };

    return (
        <div className="App">
            {showSplash ? (
                <SplashScreen onSplashEnd={handleSplashEnd} />  // Show splash screen
            ) : (
                <div className="min-h-screen flex items-center justify-center bg-gray-100">
                    <ConfigForm onSubmit={handleConfigSubmit} />  // Show main app
                </div>
            )}
        </div>
    );
};

export default App;
