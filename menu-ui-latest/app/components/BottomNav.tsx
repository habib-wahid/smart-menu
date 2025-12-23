"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Home, Tag, ShoppingCart, User } from "lucide-react";

interface NavItem {
  icon: typeof Home;
  label: string;
  href: string;
}

const navItems: NavItem[] = [
  { icon: Home, label: "Home", href: "/menu" },
  { icon: Tag, label: "Offers", href: "/offers" },
  { icon: ShoppingCart, label: "Cart", href: "/cart" },
  { icon: User, label: "Profile", href: "/profile" },
];

export default function BottomNav() {
  const pathname = usePathname();

  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-gray-100 border-t border-gray-200 px-6 py-3 md:max-w-[390px] md:left-1/2 md:-translate-x-1/2">
      <div className="flex items-center justify-between">
        {navItems.map((item) => {
          const isActive = pathname === item.href;
          return (
            <Link
              key={item.label}
              href={item.href}
              className={`flex flex-col items-center gap-1 p-2 rounded-xl transition-colors ${
                isActive
                  ? "text-purple-600"
                  : "text-gray-500 hover:text-gray-700"
              }`}
            >
              <item.icon size={24} fill={isActive ? "currentColor" : "none"} />
              <span className="text-xs font-medium">{item.label}</span>
            </Link>
          );
        })}
      </div>
    </nav>
  );
}
