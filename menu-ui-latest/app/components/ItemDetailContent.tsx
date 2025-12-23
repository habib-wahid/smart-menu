"use client";

import Image from "next/image";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { Minus, Plus, Star } from "lucide-react";
import { type Item, type Addon } from "@/services/itemService";
import { useCart, type SelectedAddon } from "@/app/context/CartContext";

interface ItemDetailContentProps {
  item: Item;
  addons: Addon[];
}

export default function ItemDetailContent({ item, addons }: ItemDetailContentProps) {
  const [quantity, setQuantity] = useState(1);
  const [selectedAddons, setSelectedAddons] = useState<Set<number>>(new Set());
  const { addToCart } = useCart();
  const router = useRouter();

  const handleQuantityChange = (delta: number) => {
    setQuantity((prev) => Math.max(1, prev + delta));
  };

  const toggleAddon = (addonId: number) => {
    setSelectedAddons((prev) => {
      const newSet = new Set(prev);
      if (newSet.has(addonId)) {
        newSet.delete(addonId);
      } else {
        newSet.add(addonId);
      }
      return newSet;
    });
  };

  const getAddonTotal = () => {
    return addons
      .filter((addon) => selectedAddons.has(addon.id))
      .reduce((sum, addon) => sum + addon.price, 0);
  };

  const totalPrice = (item.price * quantity) + getAddonTotal();

  const handleAddToCart = () => {
    // Add main item to cart with selected addons
    addToCart(
      {
        id: item.id,
        name: item.name,
        price: item.price,
        quantity: quantity,
        image: item.filePath || "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
        addons: [], // Will be populated below
      },
      // Add selected addons with quantity
      addons
        .filter((addon) => selectedAddons.has(addon.id))
        .map((addon): SelectedAddon => ({
          id: addon.id,
          name: addon.name,
          price: addon.price,
          image: addon.filePath || "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=200&q=80",
          quantity: 1,
        }))
    );

    // Navigate to cart
    router.push("/cart");
  };

  return (
    <div className="flex flex-col min-h-screen">
      {/* Product Image Section */}
      <div className="relative h-80 bg-gradient-to-b from-[#87CEEB] to-[#9B59B6]">
        <div className="absolute inset-0 flex items-center justify-center">
          <div className="relative w-72 h-72 rounded-full overflow-hidden border-4 border-white shadow-xl">
            <Image
              src={item.filePath}
              alt={item.name}
              fill
              className="object-cover"
              priority
            />
          </div>
        </div>
      </div>

      {/* Content Section */}
      <div className="flex-1 px-5 pt-6 pb-24">
        {/* Rating and Price Row */}
        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-1 bg-gray-100 px-3 py-1.5 rounded-full">
            <Star size={16} className="fill-yellow-400 text-yellow-400" />
            <span className="text-sm font-semibold">{item.rating || 4.8}</span>
          </div>
          <span className="text-2xl font-bold text-purple-600">₹{item.price}</span>
        </div>

        {/* Product Name */}
        <h1 className="text-2xl font-bold text-gray-900 mb-4">{item.name}</h1>

        {/* Quantity Selector */}
        <div className="flex items-center gap-4 mb-6">
          <span className="text-gray-600 font-medium">Quantity</span>
          <div className="flex items-center gap-3">
            <button
              onClick={() => handleQuantityChange(-1)}
              className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center hover:bg-gray-200 transition-colors"
            >
              <Minus size={18} className="text-gray-600" />
            </button>
            <span className="text-xl font-bold w-8 text-center">{quantity}</span>
            <button
              onClick={() => handleQuantityChange(1)}
              className="w-10 h-10 rounded-full bg-purple-600 flex items-center justify-center hover:bg-purple-700 transition-colors"
            >
              <Plus size={18} className="text-white" />
            </button>
          </div>
        </div>

        {/* Description */}
        <div className="mb-6">
          <h2 className="text-lg font-bold text-gray-900 mb-2">Description</h2>
          <p className="text-gray-500 leading-relaxed">
            {item.description || "Delicious and freshly prepared with premium ingredients. Perfect for any meal of the day."}
          </p>
        </div>

        {/* Add Ons Section */}
        {addons.length > 0 && (
          <div className="mb-6">
            <h2 className="text-lg font-bold text-gray-900 mb-4">Add Ons</h2>
            <div className="flex gap-4 overflow-x-auto pb-2">
              {addons.map((addon) => (
                <div
                  key={addon.id}
                  className="flex-shrink-0 relative"
                  onClick={() => toggleAddon(addon.id)}
                >
                  <div
                    className={`w-24 h-24 rounded-2xl overflow-hidden cursor-pointer transition-all ${
                      selectedAddons.has(addon.id)
                        ? "ring-2 ring-purple-600 ring-offset-2"
                        : ""
                    }`}
                  >
                    <Image
                      src={addon.filePath ? addon.filePath : "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=200&q=80"}
                      alt={addon.name}
                      width={96}
                      height={96}
                      className="object-cover w-full h-full"
                    />
                  </div>
                  <button
                    className={`absolute -bottom-2 -right-2 w-8 h-8 rounded-full flex items-center justify-center shadow-md transition-colors ${
                      selectedAddons.has(addon.id)
                        ? "bg-purple-600 text-white"
                        : "bg-green-500 text-white hover:bg-green-600"
                    }`}
                  >
                    <Plus size={16} className={selectedAddons.has(addon.id) ? "rotate-45" : ""} />
                  </button>
                  <p className="text-center text-xs mt-2 text-gray-600 font-medium">
                    {addon.name}
                  </p>
                  <p className="text-center text-xs text-purple-600 font-bold">
                    +₹{addon.price}
                  </p>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Fixed Bottom CTA */}
      <div className="fixed bottom-0 left-0 right-0 p-5 bg-white border-t border-gray-100 md:max-w-[390px] md:left-1/2 md:-translate-x-1/2">
        <button 
          onClick={handleAddToCart}
          className="w-full py-4 bg-purple-600 text-white text-lg font-bold rounded-full hover:bg-purple-700 transition-colors active:scale-[0.98]"
        >
          Add to Cart - ₹{totalPrice}
        </button>
      </div>
    </div>
  );
}
