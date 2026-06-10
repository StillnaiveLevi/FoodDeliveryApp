export type CartItem = {
  id: number;
  restaurantId: number;
  name: string;
  description?: string;
  price: number;
  imageUrl?: string;
  quantity: number;
};

const CART_KEY = 'foodDeliveryCart';

export const getCart = (): CartItem[] => {
  const rawCart = localStorage.getItem(CART_KEY);

  if (!rawCart) {
    return [];
  }

  try {
    return JSON.parse(rawCart) as CartItem[];
  } catch {
    localStorage.removeItem(CART_KEY);
    return [];
  }
};

export const saveCart = (items: CartItem[]) => {
  localStorage.setItem(CART_KEY, JSON.stringify(items));
};

export const clearCart = () => {
  localStorage.removeItem(CART_KEY);
};

export const addCartItem = (item: Omit<CartItem, 'quantity'>) => {
  const cart = getCart();
  const existingItem = cart.find((cartItem) => cartItem.id === item.id);

  if (existingItem) {
    const updatedCart = cart.map((cartItem) =>
      cartItem.id === item.id ? { ...cartItem, quantity: cartItem.quantity + 1 } : cartItem,
    );
    saveCart(updatedCart);
    return updatedCart;
  }

  const updatedCart = [...cart, { ...item, quantity: 1 }];
  saveCart(updatedCart);
  return updatedCart;
};
