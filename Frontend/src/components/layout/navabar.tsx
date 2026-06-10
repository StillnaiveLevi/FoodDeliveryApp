import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

const Navbar = () => {
  const [user, setUser] = useState<{ fullName: string } | null>(null);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const token = localStorage.getItem('token');
    const fullName = localStorage.getItem('fullName');
    setUser(token && fullName ? { fullName } : null);
  }, [location.pathname]);

  const logout = () => {
    localStorage.clear();
    setUser(null);
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold text-orange-600">FoodDelivery</Link>

        <div className="flex items-center gap-6">
          <Link to="/" className="hover:text-orange-600">Home</Link>
          <Link to="/cart" className="hover:text-orange-600">Cart</Link>

          {user ? (
            <>
              <span className="font-medium">Hi, {user.fullName}</span>
              <button
                onClick={logout}
                className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
              >
                Logout
              </button>
            </>
          ) : (
            <Link to="/login" className="bg-orange-600 text-white px-6 py-2 rounded-lg hover:bg-orange-700">
              Login
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
