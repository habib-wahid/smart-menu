"use client";

import { Home, Tag, ShoppingCart, User } from "lucide-react";

interface NavItem {
  icon: typeof Home;
  label: string;
  active?: boolean;
}

const navItems: NavItem[] = [
  { icon: Home, label: "Home", active: true },
  { icon: Tag, label: "Offers" },
  { icon: ShoppingCart, label: "Cart" },
  { icon: User, label: "Profile" },
];

export default function BottomNav() {
  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-gray-100 border-t border-gray-200 px-6 py-3 md:max-w-[390px] md:left-1/2 md:-translate-x-1/2">
      <div className="flex items-center justify-between">
        {navItems.map((item) => (
          <button
            key={item.label}
            className={`flex flex-col items-center gap-1 p-2 rounded-xl transition-colors ${
              item.active
                ? "text-purple-600"
                : "text-gray-500 hover:text-gray-700"
            }`}
          >
            <item.icon size={24} fill={item.active ? "currentColor" : "none"} />
            <span className="text-xs font-medium">{item.label}</span>
          </button>
        ))}
      </div>
    </nav>
  );
}
