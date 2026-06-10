import { Link } from 'react-router-dom';

const CartPage = () => {
  return (
    <main className="max-w-4xl mx-auto px-6 py-12">
      <div className="bg-white rounded-2xl shadow-xl p-8">
        <h1 className="text-3xl font-bold mb-4">Your Cart</h1>
        <p className="text-gray-600 mb-8">
          Cart persistence is not connected yet. Add-to-cart currently only lives inside the menu page state.
        </p>
        <Link
          to="/restaurants/1/menu"
          className="inline-block bg-orange-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-orange-700"
        >
          Browse Menu
        </Link>
      </div>
    </main>
  );
};

export default CartPage;
