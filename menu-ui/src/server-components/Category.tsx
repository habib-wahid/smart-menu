import { CategoryResponse } from "@/Responses";
import CategoryList from "./CategoryList";


const categoryIconMap: Record<string, string> = {
  all: '🍽️',          
  breakfast: '🥞',
  meal: '🥪',          // 🥪 sandwich instead of 🍽️ for lunch
  soups: '🍖',
  snacks: '🍿',
  pizza: '🍕',
  burger: '🍔',
  pasta: '🍝',
  sushi: '🍣',
  'heavy food': '🍖',
  'light food': '🥗',
  desserts: '🍰',
  beverages: '🥤',
  vegetarian: '🥕',
  spicy: '🌶️',
};

export default async function Category() {

    const response = await fetch('http://localhost:8080/api/category', {
        cache: "no-cache"
    });
    
    if (!response.ok) {
        throw new Error("Failed to fetch categories");
    }
    
    const data : CategoryResponse[] = await response.json();

    console.log("Categories: ", data);

    const enrichedCategories = data.map((cat: any) => {
        const nameKey = cat.name.toLowerCase();
        return {
        ...cat,
        icon: categoryIconMap[nameKey] || '🍽️',
        };
    });

    // Add 'All' manually at the beginning
    const categories = [
        { id: -1, name: 'All', icon: categoryIconMap['all'] },
        ...enrichedCategories
    ];
    
    return (
            <CategoryList categories={categories} />
    );
    }