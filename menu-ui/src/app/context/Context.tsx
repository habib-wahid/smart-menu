"use client";

import { createContext, useContext, useEffect } from "react";
import { useState } from "react";

export interface OrderAddon {
    addonId: number;
    quantity: number;
}

export interface OrderItem {
    itemId: number;
    quantity: number;
    addons?: OrderAddon[];
}

export interface CartItem {
    id: number;
    name: string;
    price: number;
    quantity: number;
    image: string;
}

interface CartContextType {
    cart: CartItem[];
    addToCart: (item: CartItem) => void;
    removeFromCart: (id: number) => void;
}

export const CartContext = createContext<CartContextType | null>(null);

export const CartProvider = ({children} : {children: React.ReactNode}) => {
    const [cart, setCart] = useState<CartItem[]>([]);

    useEffect(() => {
        const storedCart = localStorage.getItem("cart");
        if (storedCart) {
            setCart(JSON.parse(storedCart));
        }
    }, []);

    useEffect(() => {
        localStorage.setItem("cart", JSON.stringify(cart));
    }, [cart]);

    const addToCart = (item: CartItem) => {
        setCart((prev) => {
            const existingItem = prev.find((i) => i.id === item.id);
            if (existingItem) {
                return prev.map((i) => {
                    if (i.id === item.id) {
                        return {
                            ...i,
                            quantity: i.quantity + item.quantity,
                        };
                    }
                    return i;
                });
            }
            return [...prev, item];
        });
    }

    const removeFromCart = (id: number) => {
        setCart((prev) => {
            const existingItem = prev.find((i) => i.id === id);
            if (existingItem) {
                if (existingItem.quantity === 1) {
                    return prev.filter((i) => i.id !== id);
                }
                return prev.map((i) => {
                    if (i.id === id){
                        return {
                            ...i,
                            quantity: i.quantity - 1,
                        }
                    }
                    return i;
                });
            }

            return prev;
        })
    }

    return (
        <CartContext.Provider value={{cart, addToCart, removeFromCart}}>
            {children}
        </CartContext.Provider>
    )
}

// export const useCart = () => {
//     const context = useContext(CartContext);
//     if (!context) {
//         throw new Error("useCart must be used within a CartProvider");
//     }
//     return context;
// }

