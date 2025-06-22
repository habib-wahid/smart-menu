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
    addons: OrderAddon[];
}

interface OrderContextType {
    order: OrderItem[];
    addToOrder: (item: OrderItem) => void;
    removeFromOrder: (id: number) => void;
    addAddonsToItem: (itemId: number, addons: OrderAddon) => void;
}

export const OrderContext = createContext<OrderContextType | null>(null);

export const OrderProvider = ({ children }: { children: React.ReactNode }) => {
    const [order, setOrder] = useState<OrderItem[]>([]);
    const [isLoaded, setIsLoaded] = useState(false);

    useEffect(() => {
        try {
            const storedOrder = localStorage.getItem("order");
            console.log("storedCart", storedOrder);
            if (storedOrder) {
                setOrder(JSON.parse(storedOrder));
            }
        } catch (error) {
            console.error("Failed to parse order from localStorage", error);
        } finally {
            setIsLoaded(true);
        }
    }, []);

    useEffect(() => {
        console.log("order", order);
        if (isLoaded) {
            localStorage.setItem("order", JSON.stringify(order));
        }
    }, [order, isLoaded]);

    const addToOrder = (item: OrderItem) => {
        setOrder((prev) => {
            const existingItem = prev.find((i) => i.itemId === item.itemId);
            if (existingItem) {
                return prev.map((i) => {
                    if (i.itemId === item.itemId) {
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
    };

    const addAddonsToItem = (itemId: number, addons: OrderAddon) => {
        setOrder(prev => 
            prev.map(item => {
                if (item.itemId !== itemId) return item;
                
                const updatedAddons = [...item.addons];
                
                const existingAddonIndex = updatedAddons.findIndex(a => a.addonId === addons.addonId);
                
                if (existingAddonIndex >= 0) {
                    updatedAddons[existingAddonIndex] = {
                        ...updatedAddons[existingAddonIndex],
                        quantity: updatedAddons[existingAddonIndex].quantity + addons.quantity
                    };
                } else {
                    updatedAddons.push(addons);
                }

                return { ...item, addons: updatedAddons };
            })
        );
    };

    const removeFromOrder = (id: number) => {
        setOrder((prev) => prev.filter((item) => item.itemId !== id));
    };

    return (
        <OrderContext.Provider value={{ order, addToOrder, removeFromOrder, addAddonsToItem }}>
            {children}
        </OrderContext.Provider>
    );
};


export const useOrder = () => {
    const context = useContext(OrderContext);
    if (!context) {
        throw new Error("useOrder must be used within an OrderProvider");
    }
    return context;
};