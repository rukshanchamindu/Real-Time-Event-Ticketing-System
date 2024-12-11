import { useEffect } from 'react';

const useWebSocket = (url, onMessage, onClose, onError) => {
  useEffect(() => {
    const socket = new WebSocket(url);

    socket.onmessage = onMessage;
    socket.onclose = onClose;
    socket.onerror = onError;

    // Cleanup on unmount
    return () => {
      socket.close();
    };
  }, [url, onMessage, onClose, onError]);
};

export default useWebSocket;
