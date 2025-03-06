"use client";

import { createContext, useContext, useEffect } from "react";
import { useState } from "react";

export interface CartItem {
    id: number;
    name: string;
    price: number;
    quantity: number;
    addOns? : string[];
}

interface CartContextType {
    cart: CartItem[];
    addToCart: (item: CartItem) => void;
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
        console.log("Adding to cart", item, " ", cart.length);
        setCart([...cart, item]);
    }

    return (
        <CartContext.Provider value={{cart, addToCart}}>
            {children}
        </CartContext.Provider>
    )
}

export const useCart = () => {
    const context = useContext(CartContext);
    if (!context) {
        throw new Error("useCart must be used within a CartProvider");
    }
    return context;
}

