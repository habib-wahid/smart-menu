"use client";

import { useState } from "react";
import { LayoutGrid } from "lucide-react";
import { type Category } from "@/services/categoryService";
import { getIconForCategory } from "@/utils/iconMap";
import CategoryTab from "./CategoryTab";

interface CategoryTabsProps {
  categories: Category[];
}

export default function CategoryTabs({ categories }: CategoryTabsProps) {
  const [activeCategory, setActiveCategory] = useState("All");

  // Combine "All" with fetched categories and map icons
  const allCategories = [
    { id: 0, name: "All", icon: LayoutGrid },
    ...categories.map((cat) => ({
      ...cat,
      icon: getIconForCategory(cat.name),
    })),
  ];

  return (
    <div className="px-5 mb-6">
      <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-hide">
        {allCategories.map((category) => (
          <CategoryTab
            key={category.id}
            icon={category.icon}
            label={category.name}
            active={activeCategory === category.name}
            onClick={() => setActiveCategory(category.name)}
          />
        ))}
      </div>
    </div>
  );
}
