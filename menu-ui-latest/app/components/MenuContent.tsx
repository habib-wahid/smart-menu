"use client";

import { useState, useEffect } from "react";
import { type Category } from "@/services/categoryService";
import { type PopularItem } from "@/services/popularItemService";
import { type Item, fetchItemsByCategory } from "@/services/itemService";
import CategoryTabs from "./CategoryTabs";
import PopularSection from "./PopularSection";

interface MenuContentProps {
  categories: Category[];
  initialPopularItems: PopularItem[];
}

export default function MenuContent({ 
  categories, 
  initialPopularItems 
}: MenuContentProps) {
  const [activeCategory, setActiveCategory] = useState<number | null>(null);
  const [items, setItems] = useState<PopularItem[] | Item[]>(initialPopularItems);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const loadItems = async () => {
      if (activeCategory === null) {
        // "All" selected - show initial popular items
        setItems(initialPopularItems);
        return;
      }

      setIsLoading(true);
      try {
        const categoryItems = await fetchItemsByCategory(activeCategory);
        setItems(categoryItems);
      } catch (error) {
        console.error("Error loading category items:", error);
        setItems([]);
      } finally {
        setIsLoading(false);
      }
    };

    loadItems();
  }, [activeCategory, initialPopularItems]);

  const handleCategoryChange = (categoryId: number | null) => {
    setActiveCategory(categoryId);
  };

  // Determine section title based on active category
  const getSectionTitle = () => {
    if (activeCategory === null) {
      return "Popular";
    }
    const category = categories.find((c) => c.id === activeCategory);
    return category ? category.name : "Items";
  };

  return (
    <>
      <CategoryTabs 
        categories={categories} 
        activeCategory={activeCategory}
        onCategoryChange={handleCategoryChange}
      />
      <PopularSection 
        items={items} 
        isLoading={isLoading}
        title={getSectionTitle()}
      />
    </>
  );
}
