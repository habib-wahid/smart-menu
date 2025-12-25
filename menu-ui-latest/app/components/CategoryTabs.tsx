"use client";

import { LayoutGrid } from "lucide-react";
import { type Category } from "@/services/categoryService";
import { getIconForCategory } from "@/utils/iconMap";
import CategoryTab from "./CategoryTab";

interface CategoryTabsProps {
  categories: Category[];
  activeCategory: number | null; // null means "All"
  onCategoryChange: (categoryId: number | null) => void;
}

export default function CategoryTabs({ 
  categories, 
  activeCategory,
  onCategoryChange 
}: CategoryTabsProps) {
  // Combine "All" with fetched categories and map icons
  const allCategories = [
    { id: null as number | null, name: "All", icon: LayoutGrid },
    ...categories.map((cat) => ({
      id: cat.id as number | null,
      name: cat.name,
      icon: getIconForCategory(cat.name),
    })),
  ];

  return (
    <div className="px-5 mb-6">
      <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-hide">
        {allCategories.map((category) => (
          <CategoryTab
            key={category.id ?? "all"}
            icon={category.icon}
            label={category.name}
            active={activeCategory === category.id}
            onClick={() => onCategoryChange(category.id)}
          />
        ))}
      </div>
    </div>
  );
}
