import Image from "next/image";
import { Menu, Percent } from "lucide-react";
import { fetchCategories } from "@/services/categoryService";
import { fetchPopularItems } from "@/services/popularItemService";
import SearchBar from "@/app/components/SearchBar";
import MenuContent from "@/app/components/MenuContent";
import FoodCard from "@/app/components/FoodCard";
import BottomNav from "@/app/components/BottomNav";

// Static special items (could also come from API)
const specialItems = [
  {
    id: 1,
    name: "Pasta Carbonara",
    price: 249,
    image: "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=400&q=80",
    isVeg: false,
    rating: 4.6,
    showRating: true,
  },
  {
    id: 2,
    name: "Mushroom Soup",
    price: 129,
    image: "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400&q=80",
    isVeg: true,
    rating: 4.4,
    showRating: false,
  },
];

// Server Component - fetches data on the server
export default async function MenuPage() {
  // Fetch data in parallel on the server
  const [categories, popularItems] = await Promise.all([
    fetchCategories(),
    fetchPopularItems(),
  ]);

  return (
    <div className="min-h-screen bg-gray-200 flex justify-center">
      <div className="w-full max-w-[390px] bg-white min-h-screen relative pb-24">
        {/* Header - Static, Server Rendered */}
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

        {/* Search Bar - Client Component */}
        <SearchBar />

        {/* Menu Content - Category Tabs + Items Section (Client Component) */}
        <MenuContent categories={categories} initialPopularItems={popularItems} />

        {/* Promotion Banner - Static, Server Rendered */}
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

        {/* The Food's Special Section - Static, Server Rendered */}
        <section className="px-5 mb-6">
          <h2 className="text-lg font-bold text-gray-900 mb-4">
            The Food&apos;s Special
          </h2>
          <div className="grid grid-cols-2 gap-4">
            {specialItems.map((item) => (
              <FoodCard key={item.id} {...item} />
            ))}
          </div>
        </section>

        {/* Bottom Navigation - Client Component */}
        <BottomNav />
      </div>
    </div>
  );
}
