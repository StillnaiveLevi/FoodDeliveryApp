import { Link } from 'react-router-dom';
import heroImage from '../../assets/hero.png';

const CustomerHome = () => {
  const featuredCategories = ['Burgers', 'Pizza', 'Chicken', 'Desserts'];
  const steps = [
    { title: 'Choose a meal', text: 'Browse restaurant menus and pick what you are craving.' },
    { title: 'Place your order', text: 'Add meals to your cart and confirm your delivery address.' },
    { title: 'Track delivery', text: 'Follow your order status from kitchen to doorstep.' },
  ];

  return (
    <main className="min-h-screen bg-gray-50">
      <section className="max-w-7xl mx-auto px-6 py-12 lg:py-16">
        <div className="grid gap-10 lg:grid-cols-[1.05fr_0.95fr] items-center">
          <div>
            <p className="inline-flex rounded-full bg-orange-100 px-4 py-2 text-sm font-semibold text-orange-700 mb-5">
              Fresh meals, delivered fast
            </p>
            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-gray-950 leading-tight mb-6">
              Order your favorite meals from nearby restaurants.
            </h1>
            <p className="text-lg text-gray-600 max-w-2xl mb-8">
              Browse menus, build your cart, place an order, and track delivery from one simple customer dashboard.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 mb-10">
              <Link
                to="/restaurants/1/menu"
                className="inline-flex justify-center bg-orange-600 text-white px-7 py-3 rounded-lg font-semibold shadow-sm hover:bg-orange-700"
              >
                Browse Menu
              </Link>
              <Link
                to="/cart"
                className="inline-flex justify-center border border-gray-300 bg-white text-gray-900 px-7 py-3 rounded-lg font-semibold hover:border-orange-500 hover:text-orange-600"
              >
                View Cart
              </Link>
            </div>

            <div className="grid grid-cols-3 gap-4 max-w-lg">
              <div>
                <p className="text-2xl font-bold text-gray-950">30+</p>
                <p className="text-sm text-gray-500">Menu items</p>
              </div>
              <div>
                <p className="text-2xl font-bold text-gray-950">24/7</p>
                <p className="text-sm text-gray-500">Ordering</p>
              </div>
              <div>
                <p className="text-2xl font-bold text-gray-950">Live</p>
                <p className="text-sm text-gray-500">Tracking</p>
              </div>
            </div>
          </div>

          <div className="relative">
            <img
              src={heroImage}
              alt="Food delivery meal"
              className="w-full h-[360px] sm:h-[460px] object-cover rounded-2xl shadow-2xl"
            />
            <div className="absolute left-5 right-5 bottom-5 rounded-xl bg-white/95 p-5 shadow-xl">
              <div className="flex items-start justify-between gap-4">
                <div>
                  <p className="text-sm font-semibold text-orange-600">Popular today</p>
                  <h2 className="text-xl font-bold text-gray-950">Chef's special combo</h2>
                  <p className="text-sm text-gray-500 mt-1">Ready for pickup or delivery.</p>
                </div>
                <span className="rounded-full bg-green-100 px-3 py-1 text-sm font-semibold text-green-700">
                  Open
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="bg-white border-y border-gray-200">
        <div className="max-w-7xl mx-auto px-6 py-8">
          <div className="flex flex-col gap-5 lg:flex-row lg:items-center lg:justify-between">
            <div>
              <h2 className="text-2xl font-bold text-gray-950">Explore categories</h2>
              <p className="text-gray-500 mt-1">Start with a favorite and discover what is available.</p>
            </div>
            <div className="flex flex-wrap gap-3">
              {featuredCategories.map((category) => (
                <Link
                  key={category}
                  to="/restaurants/1/menu"
                  className="rounded-full border border-gray-200 bg-gray-50 px-5 py-2 font-medium text-gray-700 hover:border-orange-500 hover:text-orange-600"
                >
                  {category}
                </Link>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="max-w-7xl mx-auto px-6 py-12">
        <div className="grid gap-5 md:grid-cols-3">
          {steps.map((step, index) => (
            <div key={step.title} className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
              <div className="mb-5 flex h-10 w-10 items-center justify-center rounded-full bg-orange-100 font-bold text-orange-700">
                {index + 1}
              </div>
              <h3 className="text-lg font-bold text-gray-950">{step.title}</h3>
              <p className="text-gray-500 mt-2">{step.text}</p>
            </div>
          ))}
        </div>

        <div className="mt-10 rounded-2xl bg-gray-950 px-6 py-8 text-white sm:px-8">
          <div className="flex flex-col gap-5 md:flex-row md:items-center md:justify-between">
            <div>
              <h2 className="text-2xl font-bold">Ready to place your first order?</h2>
              <p className="text-gray-300 mt-1">Open the sample restaurant menu and test the cart flow.</p>
            </div>
            <Link
              to="/restaurants/1/menu"
              className="inline-flex justify-center rounded-lg bg-orange-600 px-6 py-3 font-semibold text-white hover:bg-orange-700"
            >
              Start Ordering
            </Link>
          </div>
        </div>
      </section>
    </main>
  );
};

export default CustomerHome;
