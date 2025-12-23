"use client";

import Image from "next/image";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#87CEEB] to-[#E20BF4] flex flex-col items-center px-4 py-8">
      {/* Restaurant Name */}
      <h1 className="text-3xl md:text-5xl font-bold text-white text-center mt-8 md:mt-16 drop-shadow-lg">
        Zing Restaurant
      </h1>

      {/* Restaurant Image */}
      <div className="mt-8 md:mt-12 w-full max-w-sm md:max-w-md lg:max-w-lg">
        <div className="relative aspect-square rounded-2xl overflow-hidden shadow-2xl">
          <Image
            src="/food-bowl.svg"
            alt="Restaurant"
            fill
            className="object-cover"
            priority
          />
        </div>
      </div>

      {/* Get Started Button */}
      <button
        className="mt-10 md:mt-14 px-10 py-4 bg-white text-black text-lg md:text-xl font-semibold rounded-full shadow-lg hover:bg-gray-100 hover:scale-105 transition-all duration-300 active:scale-95"
        onClick={() => router.push("/menu")}
      >
        Get Started
      </button>
    </div>
  );
}
