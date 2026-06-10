import { Link } from 'react-router-dom';
import heroImage from '../../assets/hero.png';

const CustomerHome = () => {
  return (
    <main className="max-w-7xl mx-auto px-6 py-12">
      <section className="grid gap-8 lg:grid-cols-[1.1fr_0.9fr] items-center">
        <div>
          <p className="text-orange-600 font-semibold mb-3">Fast food delivery</p>
          <h1 className="text-5xl font-bold text-gray-900 leading-tight mb-6">
            Order your favorite meals from nearby restaurants.
          </h1>
          <p className="text-lg text-gray-600 mb-8">
            Browse menus, add meals to your cart, and track orders from one simple dashboard.
          </p>
          <div className="flex flex-wrap gap-4">
            <Link
              to="/restaurants/1/menu"
              className="bg-orange-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-orange-700"
            >
              View Menu
            </Link>
            <Link
              to="/login"
              className="border border-orange-600 text-orange-600 px-6 py-3 rounded-lg font-semibold hover:bg-orange-50"
            >
              Login
            </Link>
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-6">
          <img
            src={heroImage}
            alt="Food delivery meal"
            className="w-full h-80 object-cover rounded-xl"
          />
        </div>
      </section>
    </main>
  );
};

export default CustomerHome;
