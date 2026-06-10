import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../../services/api';
import { addCartItem } from '../../utils/cart';

type MenuItem = {
  id: number;
  name: string;
  description?: string;
  price: number;
  imageUrl?: string;
  available: boolean;
};

const MenuPage = () => {
  const { id } = useParams();
  const restaurantId = Number(id);
  const [menu, setMenu] = useState<MenuItem[]>([]);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setError('');
    setLoading(true);

    api.get(`/restaurants/${id}/menu`)
      .then((res) => setMenu(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Could not load menu'))
      .finally(() => setLoading(false));
  }, [id]);

  const addToCart = (item: MenuItem) => {
    addCartItem({
      id: item.id,
      restaurantId,
      name: item.name,
      description: item.description,
      price: item.price,
      imageUrl: item.imageUrl,
    });
    setMessage(`${item.name} added to cart`);
  };

  return (
    <div className="max-w-6xl mx-auto p-6">
      <h1 className="text-4xl font-bold mb-8">Restaurant Menu</h1>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      {message && <p className="text-green-600 mb-4">{message}</p>}
      {loading && <p className="text-gray-600">Loading menu...</p>}
      {!loading && !error && menu.length === 0 && <p className="text-gray-600">No menu items found.</p>}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {menu.map(item => (
          <div key={item.id} className="bg-white rounded-xl shadow p-6">
            <img src={item.imageUrl || '/placeholder.jpg'} alt={item.name} className="w-full h-48 object-cover rounded-lg" />
            <h3 className="text-xl font-semibold mt-4">{item.name}</h3>
            <p className="text-gray-600 mt-1">{item.description}</p>
            <div className="flex justify-between items-center mt-4">
              <span className="text-2xl font-bold">${item.price}</span>
              <button
                onClick={() => addToCart(item)}
                disabled={!item.available}
                className="bg-orange-600 text-white px-6 py-2 rounded-lg hover:bg-orange-700"
              >
                {item.available ? 'Add to Cart' : 'Unavailable'}
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MenuPage;
