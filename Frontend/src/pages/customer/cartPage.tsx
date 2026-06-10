import { useEffect, useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { clearCart, getCart, saveCart, type CartItem } from '../../utils/cart';

const CartPage = () => {
  const [items, setItems] = useState<CartItem[]>([]);
  const [deliveryAddress, setDeliveryAddress] = useState('');
  const [promoCode, setPromoCode] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setItems(getCart());
  }, []);

  const subtotal = useMemo(
    () => items.reduce((total, item) => total + item.price * item.quantity, 0),
    [items],
  );

  const updateQuantity = (itemId: number, quantity: number) => {
    const updatedItems = items
      .map((item) => (item.id === itemId ? { ...item, quantity } : item))
      .filter((item) => item.quantity > 0);

    setItems(updatedItems);
    saveCart(updatedItems);
  };

  const handleCheckout = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!localStorage.getItem('token')) {
      navigate('/login');
      return;
    }

    if (items.length === 0) {
      setError('Your cart is empty');
      return;
    }

    const restaurantId = items[0].restaurantId;
    const hasMixedRestaurants = items.some((item) => item.restaurantId !== restaurantId);

    if (hasMixedRestaurants) {
      setError('Please order from one restaurant at a time');
      return;
    }

    setLoading(true);

    try {
      const res = await api.post('/customer/orders', {
        restaurantId,
        deliveryAddress,
        promoCode: promoCode.trim() || undefined,
        items: items.map((item) => ({
          menuItemId: item.id,
          quantity: item.quantity,
        })),
      });

      clearCart();
      navigate(`/orders/${res.data.orderId}/track`);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Could not place order');
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="max-w-4xl mx-auto px-6 py-12">
      <div className="bg-white rounded-2xl shadow-xl p-8">
        <h1 className="text-3xl font-bold mb-6">Your Cart</h1>

        {error && <p className="text-red-500 mb-4">{error}</p>}

        {items.length === 0 ? (
          <div>
            <p className="text-gray-600 mb-8">Your cart is empty.</p>
            <Link
              to="/restaurants/1/menu"
              className="inline-block bg-orange-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-orange-700"
            >
              Browse Menu
            </Link>
          </div>
        ) : (
          <form onSubmit={handleCheckout} className="space-y-6">
            <div className="space-y-4">
              {items.map((item) => (
                <div key={item.id} className="flex justify-between gap-4 border-b pb-4">
                  <div>
                    <h2 className="font-semibold">{item.name}</h2>
                    <p className="text-gray-600">${item.price.toFixed(2)} each</p>
                  </div>
                  <div className="flex items-center gap-3">
                    <button
                      type="button"
                      onClick={() => updateQuantity(item.id, item.quantity - 1)}
                      className="px-3 py-1 border rounded"
                    >
                      -
                    </button>
                    <span>{item.quantity}</span>
                    <button
                      type="button"
                      onClick={() => updateQuantity(item.id, item.quantity + 1)}
                      className="px-3 py-1 border rounded"
                    >
                      +
                    </button>
                  </div>
                </div>
              ))}
            </div>

            <input
              type="text"
              placeholder="Delivery address"
              value={deliveryAddress}
              onChange={(e) => setDeliveryAddress(e.target.value)}
              className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
              required
            />

            <input
              type="text"
              placeholder="Promo code"
              value={promoCode}
              onChange={(e) => setPromoCode(e.target.value)}
              className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
            />

            <div className="flex justify-between text-xl font-bold">
              <span>Total</span>
              <span>${subtotal.toFixed(2)}</span>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-orange-600 text-white py-3 rounded-lg font-semibold hover:bg-orange-700"
            >
              {loading ? 'Placing order...' : 'Place Order'}
            </button>
          </form>
        )}
      </div>
    </main>
  );
};

export default CartPage;
