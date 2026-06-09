import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../../services/api';
import { io } from 'socket.io-client';

const OrderTracking = () => {
  const { orderId } = useParams();
  const [tracking, setTracking] = useState<any>(null);
  const [status, setStatus] = useState<string>("");

  useEffect(() => {
  const fetchOrder = async () => {
    const res = await api.get(`/tracking/orders/${orderId}`);
    setTracking(res.data);
    setStatus(res.data.status);
  };
  fetchOrder();

  const socket = io('http://localhost:8080', { path: '/ws-order-tracking' });

  socket.on(`order/${orderId}`, (data: any) => {
    setTracking(data);
    setStatus(data.status);
  });

  return () => {
    socket.disconnect();
  };
}, [orderId]);

  const statusSteps = ["PENDING", "CONFIRMED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED"];

  return (
    <div className="max-w-2xl mx-auto p-6">
      <div className="bg-white rounded-2xl shadow-xl p-8">
        <h1 className="text-3xl font-bold mb-8 text-center">Order Tracking</h1>

        <div className="space-y-8">
          {/* Progress Bar */}
          <div className="relative">
            {statusSteps.map((step, index) => (
              <div key={index} className="flex items-center mb-6">
                <div className={`w-8 h-8 rounded-full flex items-center justify-center 
                  ${statusSteps.indexOf(status) >= index ? 'bg-green-500 text-white' : 'bg-gray-200'}`}>
                  {index + 1}
                </div>
                <div className="ml-4">
                  <p className="font-semibold">{step}</p>
                </div>
              </div>
            ))}
          </div>

          {/* Live Status */}
          <div className="bg-green-50 border border-green-200 p-6 rounded-xl">
            <p className="text-xl font-semibold text-green-700">
              Current Status: <span className="capitalize">{status}</span>
            </p>
            <p className="mt-2 text-gray-600">
              {tracking?.currentStatusMessage || "Your order is being processed"}
            </p>
          </div>

          <div className="text-center text-sm text-gray-500">
            Order #{orderId} • Estimated Delivery: {tracking?.estimatedDeliveryTime}
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderTracking;