"use client";

import Link from "next/link";
import { type PopularItem } from "@/services/popularItemService";
import FoodCard from "./FoodCard";

interface PopularSectionProps {
  items: PopularItem[];
}

export default function PopularSection({ items }: PopularSectionProps) {
  return (
    <section className="px-5 mb-6">
      <h2 className="text-lg font-bold text-gray-900 mb-4">Popular</h2>
      <div className="max-h-80 overflow-y-auto pr-1">
        {items.length === 0 ? (
          <p className="text-gray-500 text-center py-8">No popular items available</p>
        ) : (
          <div className="grid grid-cols-2 gap-4">
            {items.map((item) => (
              <Link key={item.id} href={`/item/${item.id}`}>
                <FoodCard
                  name={item.name}
                  price={item.price}
                  image={item.filePath}
                  rating={item.rating}
                  showRating={item.rating > 0}
                  isVeg={false}
                />
              </Link>
            ))}
          </div>
        )}
      </div>
    </section>
  );
}
