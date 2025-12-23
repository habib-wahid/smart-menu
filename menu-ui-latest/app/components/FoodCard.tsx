"use client";

import Image from "next/image";
import { Plus, Star } from "lucide-react";

interface FoodCardProps {
  name: string;
  price: number;
  image: string;
  isVeg?: boolean;
  rating?: number;
  showRating?: boolean;
}

export default function FoodCard({
  name,
  price,
  image,
  isVeg = true,
  rating,
  showRating = false,
}: FoodCardProps) {
  return (
    <div className="bg-gray-50 rounded-2xl p-3 relative">
      <div className="relative aspect-square rounded-xl overflow-hidden mb-3">
        <Image src={image} alt={name} fill className="object-cover" />
        <div
          className={`absolute top-2 left-2 w-4 h-4 rounded-sm border-2 ${
            isVeg ? "border-green-600 bg-green-600" : "border-red-600 bg-red-600"
          }`}
        >
          <div className="w-2 h-2 rounded-full bg-white absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2" />
        </div>
        {showRating && rating && (
          <div className="absolute top-2 right-2 bg-white px-2 py-1 rounded-full flex items-center gap-1">
            <Star size={12} className="fill-yellow-400 text-yellow-400" />
            <span className="text-xs font-medium">{rating}</span>
          </div>
        )}
      </div>
      <h3 className="font-semibold text-gray-800 text-sm mb-1 line-clamp-2">
        {name}
</h3>
      <div className="flex items-center justify-between">
        <span className="text-purple-600 font-bold">₹{price}</span>
        <button className="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center text-white hover:bg-green-600 transition-colors">
          <Plus size={18} />
        </button>
      </div>
    </div>
  );
}
