import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { io } from 'socket.io-client';
import api from '../../services/api';

const OrderTracking = () => {
  const { orderId } = useParams();
  const [tracking, setTracking] = useState<any>(null);
  const [status, setStatus] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOrder = async () => {
      try {
        const res = await api.get(`/orders/track/${orderId}`);
        setTracking(res.data);
        setStatus(res.data.status);
      } catch (err: any) {
        setError(err.response?.data?.message || 'Could not load order tracking');
      } finally {
        setLoading(false);
      }
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

  const statusSteps = ['PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED'];

  return (
    <div className="max-w-2xl mx-auto p-6">
      <div className="bg-white rounded-2xl shadow-xl p-8">
        <h1 className="text-3xl font-bold mb-8 text-center">Order Tracking</h1>

        {loading && <p className="text-center text-gray-600">Loading order...</p>}
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}

        <div className="space-y-8">
          <div className="relative">
            {statusSteps.map((step, index) => (
              <div key={step} className="flex items-center mb-6">
                <div
                  className={`w-8 h-8 rounded-full flex items-center justify-center ${
                    statusSteps.indexOf(status) >= index ? 'bg-green-500 text-white' : 'bg-gray-200'
                  }`}
                >
                  {index + 1}
                </div>
                <div className="ml-4">
                  <p className="font-semibold">{step}</p>
                </div>
              </div>
            ))}
          </div>

          <div className="bg-green-50 border border-green-200 p-6 rounded-xl">
            <p className="text-xl font-semibold text-green-700">
              Current Status: <span className="capitalize">{status}</span>
            </p>
            <p className="mt-2 text-gray-600">
              {tracking?.currentStatusMessage || 'Your order is being processed'}
            </p>
          </div>

          <div className="text-center text-sm text-gray-500">
            Order #{orderId} - Estimated Delivery: {tracking?.estimatedDeliveryTime}
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderTracking;
