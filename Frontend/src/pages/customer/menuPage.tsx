import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../../services/api';

const MenuPage = () => {
  const { id } = useParams();
  const [menu, setMenu] = useState<any[]>([]);
  const [cart, setCart] = useState<any[]>([]);

  useEffect(() => {
    api.get(`/restaurants/${id}/menu`).then(res => setMenu(res.data));
  }, [id]);

  const addToCart = (item: any) => {
    setCart([...cart, { ...item, quantity: 1 }]);
  };

  return (
    <div className="max-w-6xl mx-auto p-6">
      <h1 className="text-4xl font-bold mb-8">Restaurant Menu</h1>
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
                className="bg-orange-600 text-white px-6 py-2 rounded-lg hover:bg-orange-700"
              >
                Add to Cart
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MenuPage;