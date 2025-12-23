"use client";

import Image from "next/image";
import { Minus, Plus, X } from "lucide-react";
import { type CartItem as CartItemType, type SelectedAddon } from "@/app/context/CartContext";

interface CartItemProps {
  item: CartItemType;
  itemIndex: number;
  onQuantityChange: (index: number, delta: number) => void;
  onRemove: (index: number) => void;
  onAddonQuantityChange: (itemIndex: number, addonId: number, delta: number) => void;
  onAddonRemove: (itemIndex: number, addonId: number) => void;
}

export default function CartItem({ 
  item, 
  itemIndex,
  onQuantityChange, 
  onRemove,
  onAddonQuantityChange,
  onAddonRemove
}: CartItemProps) {
  return (
    <div className="py-4 border-b border-gray-100">
      {/* Main Item */}
      <div className="flex items-center gap-4">
        {/* Item Image */}
        <div className="relative w-20 h-20 rounded-2xl overflow-hidden flex-shrink-0">
          <Image
            src={item.image}
            alt={item.name}
            fill
            className="object-cover"
          />
        </div>

        {/* Item Details */}
        <div className="flex-1 min-w-0">
          <h3 className="font-bold text-gray-900 text-base">
            {item.name}
            {item.variant && <span className="font-normal text-gray-500"> – {item.variant}</span>}
          </h3>
          <p className="text-purple-600 font-bold mt-1">₹{item.price}</p>

          {/* Quantity Selector */}
          <div className="flex items-center gap-3 mt-2">
            <button
              onClick={() => onQuantityChange(itemIndex, -1)}
              className="w-8 h-8 rounded-full bg-gray-100 flex items-center justify-center hover:bg-gray-200 transition-colors"
            >
              <Minus size={14} className="text-gray-600" />
            </button>
            <span className="text-base font-semibold w-6 text-center">{item.quantity}</span>
            <button
              onClick={() => onQuantityChange(itemIndex, 1)}
              className="w-8 h-8 rounded-full bg-purple-600 flex items-center justify-center hover:bg-purple-700 transition-colors"
            >
              <Plus size={14} className="text-white" />
            </button>
          </div>
        </div>

        {/* Remove Button */}
        <button
          onClick={() => onRemove(itemIndex)}
          className="w-8 h-8 rounded-full bg-gray-100 flex items-center justify-center hover:bg-red-100 transition-colors flex-shrink-0"
        >
          <X size={16} className="text-gray-500 hover:text-red-500" />
        </button>
      </div>

      {/* Addons Section */}
      {item.addons && item.addons.length > 0 && (
        <div className="mt-3 ml-6 pl-4 border-l-2 border-purple-200">
          <p className="text-xs text-gray-500 font-medium mb-2">Add-ons:</p>
          <div className="space-y-2">
            {item.addons.map((addon: SelectedAddon) => (
              <div key={addon.id} className="flex items-center gap-3">
                {/* Addon Image */}
                <div className="relative w-10 h-10 rounded-lg overflow-hidden flex-shrink-0">
                  <Image
                    src={addon.image}
                    alt={addon.name}
                    fill
                    className="object-cover"
                  />
                </div>

                {/* Addon Details */}
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-gray-700 truncate">{addon.name}</p>
                  <p className="text-xs text-purple-600 font-semibold">+₹{addon.price}</p>
                </div>

                {/* Addon Quantity Selector */}
                <div className="flex items-center gap-2">
                  <button
                    onClick={() => onAddonQuantityChange(itemIndex, addon.id, -1)}
                    className="w-6 h-6 rounded-full bg-gray-100 flex items-center justify-center hover:bg-gray-200 transition-colors"
                  >
                    <Minus size={12} className="text-gray-600" />
                  </button>
                  <span className="text-sm font-semibold w-4 text-center">{addon.quantity}</span>
                  <button
                    onClick={() => onAddonQuantityChange(itemIndex, addon.id, 1)}
                    className="w-6 h-6 rounded-full bg-purple-500 flex items-center justify-center hover:bg-purple-600 transition-colors"
                  >
                    <Plus size={12} className="text-white" />
                  </button>
                </div>

                {/* Remove Addon Button */}
                <button
                  onClick={() => onAddonRemove(itemIndex, addon.id)}
                  className="w-6 h-6 rounded-full bg-gray-100 flex items-center justify-center hover:bg-red-100 transition-colors flex-shrink-0"
                >
                  <X size={12} className="text-gray-400 hover:text-red-500" />
                </button>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
