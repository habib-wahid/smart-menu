"use client";

import { LucideIcon } from "lucide-react";

interface CategoryTabProps {
  icon: LucideIcon;
  label: string;
  active?: boolean;
  onClick?: () => void;
}

export default function CategoryTab({
  icon: Icon,
  label,
  active = false,
  onClick,
}: CategoryTabProps) {
  return (
    <button
      onClick={onClick}
      className={`flex items-center gap-2 px-4 py-2 rounded-full whitespace-nowrap transition-all ${
        active
          ? "bg-purple-600 text-white"
          : "bg-gray-100 text-gray-700 hover:bg-gray-200"
      }`}
    >
      <Icon size={18} />
      <span className="text-sm font-medium">{label}</span>
    </button>
  );
}
