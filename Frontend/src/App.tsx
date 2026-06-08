import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/layout/navabar';
import Login from './pages/auth/login';
import Register from './pages/auth/register';
import CustomerHome from './pages/customer/home';
import MenuPage from './pages/customer/menuPage';
import CartPage from './pages/customer/cartPage';
import OrderTracking from './pages/customer/orderTracking';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<CustomerHome />} />
          <Route path="/restaurants/:id/menu" element={<MenuPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/orders/:orderId/track" element={<OrderTracking />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;