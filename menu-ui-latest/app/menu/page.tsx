"use client";

import Image from "next/image";
import {
    Search,
    Menu,
    LayoutGrid,
    Percent,
} from "lucide-react";
import CategoryTab from "../components/CategoryTab";
import FoodCard from "../components/FoodCard";
import BottomNav from "../components/BottomNav";
import { useEffect, useState } from "react";
import fetchCategories, { Category } from "@/services/categoryService";
import { getIconForCategory } from "@/utils/iconMap";
import { fetchPopularItems, PopularItem } from "@/services/popularItemService";

const API_BASE_URL = "http://localhost:8080";

const specialItems = [
    {
        name: "Pasta Carbonara",
        price: 249,
        image: "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=400&q=80",
        isVeg: false,
        rating: 4.6,
        showRating: true,
    },
    {
        name: "Mushroom Soup",
        price: 129,
        image: "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400&q=80",
        isVeg: true,
        rating: 4.4,
        showRating: false,
    },
];

export default function MenuPage() {
    const [activeCategory, setActiveCategory] = useState("All");
    const [categories, setCategories] = useState<Category[]>([]);
    const [popularItems, setPopularItems] = useState<PopularItem[]>([]);
    const [loading, setLoading] = useState(true);
    const [popularLoading, setPopularLoading] = useState(true);

    useEffect(() => {
        async function loadCategories() {
            setLoading(true);
            const fetchedCategories = await fetchCategories();
            setCategories(fetchedCategories);
            setLoading(false);
        }
        loadCategories();
    }, []);

    useEffect(() => {
        async function loadPopularItems() {
            setPopularLoading(true);
            const fetchedItems = await fetchPopularItems();
            setPopularItems(fetchedItems);
            setPopularLoading(false);
        }
        loadPopularItems();
    }, []);

    const allCategories = [
        { id: 999, name: "All", icon: LayoutGrid },
        ...categories.map((cat) => ({
            ...cat,
            icon: getIconForCategory(cat.name),
        })),
    ]

    return (
        <div className="min-h-screen bg-gray-200 flex justify-center">
            <div className="w-full max-w-[390px] bg-white min-h-screen relative pb-24">
                {/* Header */}
                <header className="flex items-center justify-between px-5 pt-6 pb-4">
                    <h1 className="text-2xl font-bold text-gray-900">Menu</h1>
                    <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-full overflow-hidden bg-gray-200">
                            <Image
                                src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80"
                                alt="User avatar"
                                width={40}
                                height={40}
                                className="object-cover"
                            />
                        </div>
                        <button className="p-2 hover:bg-gray-100 rounded-lg transition-colors">
                            <Menu size={24} className="text-gray-700" />
                        </button>
                    </div>
                </header>

                {/* Search Bar */}
                <div className="px-5 mb-5">
                    <div className="relative">
                        <Search
                            size={20}
                            className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
                        />
                        <input
                            type="text"
                            placeholder="Search"
                            className="w-full bg-gray-100 rounded-full py-3 pl-12 pr-4 text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500"
                        />
                    </div>
                </div>

                {/* Category Tabs */}
                <div className="px-5 mb-6">
                    {
                        loading ? (
                            <div className="flex gap-2">
                                {[1, 2, 3, 4].map((i) => (
                                    <div
                                        key={i}
                                        className="h-10 w-20 bg-gray-200 rounded-full animate-pulse"
                                    />
                                ))}
                            </div>
                        ) : (
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
                        )
                    }

                </div>

                {/* Popular Section */}
                <section className="px-5 mb-6">
                    <h2 className="text-lg font-bold text-gray-900 mb-4">Popular</h2>
                    <div className="max-h-80 overflow-y-auto pr-1">
                        {popularLoading ? (
                            <div className="grid grid-cols-2 gap-4">
                                {[1, 2, 3, 4].map((i) => (
                                    <div
                                        key={i}
                                        className="bg-gray-100 rounded-2xl h-48 animate-pulse"
                                    />
                                ))}
                            </div>
                        ) : (
                            <div className="grid grid-cols-2 gap-4">
                                {popularItems.map((item) => (
                                    <FoodCard
                                        key={item.id}
                                        name={item.name}
                                        price={item.price}
                                        image={item.filePath}
                                        rating={item.rating}
                                        showRating={item.rating > 0}
                                        isVeg={false}
                                    />
                                ))}
                            </div>
                        )}
                    </div>
                </section>

                {/* Promotion Banner */}
                <section className="px-5 mb-6">
                    <h2 className="text-lg font-bold text-gray-900 mb-4">Promotion</h2>
                    <div className="bg-purple-600 rounded-2xl p-5 flex items-center justify-between">
                        <div>
                            <p className="text-white text-lg font-bold">Extra 10% off</p>
                            <p className="text-purple-200 text-sm">on order above ₹499</p>
                        </div>
                        <div className="w-14 h-14 bg-purple-500 rounded-full flex items-center justify-center">
                            <Percent size={28} className="text-white" />
                        </div>
                    </div>
                </section>

                {/* The Food's Special Section */}
                <section className="px-5 mb-6">
                    <h2 className="text-lg font-bold text-gray-900 mb-4">
                        The Food&apos;s Special
                    </h2>
                    <div className="grid grid-cols-2 gap-4">
                        {specialItems.map((item, index) => (
                            <FoodCard key={index} {...item} />
                        ))}
                    </div>
                </section>

                {/* Bottom Navigation */}
                <BottomNav />
            </div>
        </div>
    );
}
