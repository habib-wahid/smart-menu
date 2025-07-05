"use client"

import { useState, useRef, useEffect } from "react";
import { useRouter } from "next/navigation";


export default function CategoryList({categories} : {categories: {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
  createdAt: Date;
  icon: string;
}[]}) {

 const [isClient, setIsClient] = useState(false);
  const router = useRouter();
  const [activeCategory, setActiveCategory] = useState<number>(30);
  const scrollContainerRef = useRef<HTMLDivElement>(null);



  const handleCategoryClick = (categoryId : number) => {
    setActiveCategory(categoryId);
   
    console.log('Selected category:', categoryId);
  };


  //   const categories = [
  //   { id: 1, name: "Electronics", description: "Tech items", imageUrl: "", createdAt: new Date(), icon: "📱" },
  //   { id: 2, name: "Clothing", description: "Fashion", imageUrl: "", createdAt: new Date(), icon: "👕" },
  //   { id: 3, name: "Books", description: "Literature", imageUrl: "", createdAt: new Date(), icon: "📚" },
  //   { id: 4, name: "Sports", description: "Athletic gear", imageUrl: "", createdAt: new Date(), icon: "⚽" },
  //   { id: 5, name: "Home & Garden", description: "Living space", imageUrl: "", createdAt: new Date(), icon: "🏠" },
  //   { id: 6, name: "Beauty", description: "Cosmetics", imageUrl: "", createdAt: new Date(), icon: "💄" },
  //   { id: 7, name: "Automotive", description: "Car parts", imageUrl: "", createdAt: new Date(), icon: "🚗" },
  //   { id: 8, name: "Food", description: "Groceries", imageUrl: "", createdAt: new Date(), icon: "🍕" },
  //   { id: 9, name: "Travel", description: "Vacation gear", imageUrl: "", createdAt: new Date(), icon: "✈️" },
  //   { id: 10, name: "Music", description: "Instruments", imageUrl: "", createdAt: new Date(), icon: "🎵" },
  //   { id: 30, name: "Gaming", description: "Video games", imageUrl: "", createdAt: new Date(), icon: "🎮" }
  // ];
  

    return (
      <div className="overflow-x-auto pb-2 no-scrollbar bg-gray-100"> {/* Padding bottom for scrollbar */}
        <div className="flex space-x-3 px-5 w-max"> {/* w-max makes the container only as wide as its contents */}
          {categories.map((category) => (
            <button
              key={category.id}
              className="flex flex-col items-center justify-start rounded-2xl p-3 min-w-[120px] h-[120px] shadow-sm hover:shadow-md hover:scale-105 active:scale-95 transition-shadow"
              onClick={() => router.push(`/category/${category.id}/${category.name}`)}
            >
              <span className="text-3xl mb-1">{category.icon}</span>
              <span className="text-xs font-bold">{category.name} </span>
            </button>
          ))}
        </div>
      </div>

    );
}