import React from 'react';

const SalesReportOverlay = ({ salesReport, onClose }) => {
  // Extract the necessary data from the sales report
  const { remainingTicketsInPool, totalTickets, customers, vendors } = salesReport;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded-lg max-w-lg w-full">
        <h2 className="text-xl font-bold mb-4">Sales Report</h2>

        {/* Display Remaining Tickets and Total Tickets */}
        <div className="mb-4">
          <div className="font-semibold">Remaining Tickets :</div>
          <div>{remainingTicketsInPool} / {totalTickets}</div>
        </div>

        {/* Display Customer Details */}
        <div className="mb-4">
          <div className="font-semibold">Customer Purchases:</div>
          <ul className="list-disc pl-5">
            {Object.entries(customers).map(([customerId, customerData]) => (
              <li key={customerId}>
                <strong>{customerId}:</strong> {customerData.purchasedTickets} tickets
              </li>
            ))}
          </ul>
        </div>

        {/* Display Vendor Details */}
        <div className="mb-4">
          <div className="font-semibold">Vendor Sales:</div>
          <ul className="list-disc pl-5">
            {Object.entries(vendors).map(([vendorId, vendorData]) => (
              <li key={vendorId}>
                <strong>{vendorId}:</strong> {vendorData.totalTicketsSold} tickets, {vendorData.remainingTickets} remaining
              </li>
            ))}
          </ul>
        </div>

        {/* Close Button */}
        <button
          onClick={onClose}
          className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-md"
        >
          Close
        </button>
      </div>
    </div>
  );
};

export default SalesReportOverlay;
