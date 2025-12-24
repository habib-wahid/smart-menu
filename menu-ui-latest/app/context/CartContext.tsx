"use client";

import { createContext, useContext, useState, ReactNode } from "react";

// Types matching backend OrderRequest structure
export interface OrderAddonRequest {
  addonId: number;
  quantity: number;
}

export interface OrderItemRequest {
  itemId: number;
  quantity: number;
  orderAddons: OrderAddonRequest[];
}

export interface OrderRequest {
  phone: string;
  tableNumber: number;
  discountCode?: string;
  orderItems: OrderItemRequest[];
}

// Frontend-specific types for display
export interface SelectedAddon {
  id: number;
  name: string;
  price: number;
  image: string;
  quantity: number;
}

export interface CartItem {
  id: number;
  name: string;
  variant?: string;
  price: number;
  quantity: number;
  image: string;
  addons: SelectedAddon[];
}

interface CartContextType {
  items: CartItem[];
  phone: string;
  tableNumber: number;
  discountCode: string;
  setPhone: (phone: string) => void;
  setTableNumber: (tableNumber: number) => void;
  setDiscountCode: (code: string) => void;
  addToCart: (item: CartItem, addons?: SelectedAddon[]) => void;
  removeFromCart: (index: number) => void;
  updateQuantity: (index: number, delta: number) => void;
  updateAddonQuantity: (itemIndex: number, addonId: number, delta: number) => void;
  removeAddon: (itemIndex: number, addonId: number) => void;
  clearCart: () => void;
  placeOrder: () => Promise<{ success: boolean; message: string }>;
  totalItems: number;
  totalPrice: number;
}

const API_BASE_URL = "http://localhost:8080/api";

const CartContext = createContext<CartContextType | undefined>(undefined);

export function CartProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<CartItem[]>([]);
  const [phone, setPhone] = useState<string>("");
  const [tableNumber, setTableNumber] = useState<number>(1);
  const [discountCode, setDiscountCode] = useState<string>("");

  const addToCart = (item: CartItem, addons?: SelectedAddon[]) => {
    setItems((prev) => {
      // Create a unique key based on item id and sorted addon ids
      const addonKey = addons?.map(a => a.id).sort().join('-') || '';

      const existingIndex = prev.findIndex(
        (i) => i.id === item.id && 
               i.addons.map(a => a.id).sort().join('-') === addonKey
      );
      
      if (existingIndex >= 0) {
        // Update quantity if same item with same addons exists
        const updated = [...prev];
        updated[existingIndex].quantity += item.quantity;
        return updated;
      }
      
      // Add new item with its addons
      const newItem: CartItem = {
        ...item,
        addons: addons || [],
      };
      
      return [...prev, newItem];
    });
  };

  const removeFromCart = (index: number) => {
    setItems((prev) => prev.filter((_, i) => i !== index));
  };

  const updateQuantity = (index: number, delta: number) => {
    setItems((prev) =>
      prev.map((item, i) => {
        if (i === index) {
          const newQuantity = Math.max(1, item.quantity + delta);
          return { ...item, quantity: newQuantity };
        }
        return item;
      })
    );
  };

  const updateAddonQuantity = (itemIndex: number, addonId: number, delta: number) => {
    setItems((prev) =>
      prev.map((item, i) => {
        if (i === itemIndex) {
          const updatedAddons = item.addons.map((addon) => {
            if (addon.id === addonId) {
              const newQuantity = Math.max(1, addon.quantity + delta);
              return { ...addon, quantity: newQuantity };
            }
            return addon;
          });
          return { ...item, addons: updatedAddons };
        }
        return item;
      })
    );
  };

  const removeAddon = (itemIndex: number, addonId: number) => {
    setItems((prev) =>
      prev.map((item, i) => {
        if (i === itemIndex) {
          return {
            ...item,
            addons: item.addons.filter((addon) => addon.id !== addonId),
          };
        }
        return item;
      })
    );
  };

  const clearCart = () => {
    setItems([]);
    setDiscountCode("");
  };

  const placeOrder = async (): Promise<{ success: boolean; message: string }> => {
    if (items.length === 0) {
      return { success: false, message: "Cart is empty" };
    }

    if (!phone || phone.length < 10) {
      return { success: false, message: "Please enter a valid phone number" };
    }

    if (tableNumber <= 0) {
      return { success: false, message: "Please enter a valid table number" };
    }

    // Build OrderRequest matching backend structure
    const orderRequest: OrderRequest = {
      phone,
      tableNumber,
      discountCode: discountCode || undefined,
      orderItems: items.map((item) => ({
        itemId: item.id,
        quantity: item.quantity,
        orderAddons: item.addons.map((addon) => ({
          addonId: addon.id,
          quantity: addon.quantity || 1,
        })),
      })),
    };

    try {
      const response = await fetch(`${API_BASE_URL}/order/place`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(orderRequest),
      });

      const data = await response.json();

      if (response.ok) {
        clearCart();
        return { success: true, message: data.data || "Order placed successfully" };
      } else {
        return { success: false, message: data.message || "Failed to place order" };
      }
    } catch (error) {
      console.error("Error placing order:", error);
      return { success: false, message: "Network error. Please try again." };
    }
  };

  const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);
  const totalPrice = items.reduce((sum, item) => {
    const itemTotal = item.price * item.quantity;
    const addonsTotal = item.addons.reduce(
      (addonSum, addon) => addonSum + addon.price * (addon.quantity || 1),
      0
    );
    return sum + itemTotal + addonsTotal;
  }, 0);

  return (
    <CartContext.Provider
      value={{
        items,
        phone,
        tableNumber,
        discountCode,
        setPhone,
        setTableNumber,
        setDiscountCode,
        addToCart,
        removeFromCart,
        updateQuantity,
        updateAddonQuantity,
        removeAddon,
        clearCart,
        placeOrder,
        totalItems,
        totalPrice,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error("useCart must be used within a CartProvider");
  }
  return context;
}
