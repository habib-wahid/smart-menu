import BottomNav from "@/app/components/BottomNav";
import { Tag } from "lucide-react";

export default function OffersPage() {
  return (
    <main className="min-h-screen bg-white md:max-w-[390px] md:mx-auto md:shadow-xl">
      <div className="flex flex-col min-h-screen pb-24">
        {/* Header */}
        <header className="px-5 pt-8 pb-4">
          <h1 className="text-2xl font-bold text-gray-900">Offers</h1>
          <p className="text-gray-500 text-sm mt-1">
            Exclusive deals and discounts
          </p>
        </header>

        {/* Empty State */}
        <div className="flex-1 flex flex-col items-center justify-center px-5">
          <div className="w-24 h-24 bg-purple-100 rounded-full flex items-center justify-center mb-6">
            <Tag size={48} className="text-purple-400" />
          </div>
          
          <h2 className="text-xl font-bold text-gray-900 mb-2">
            No Offers Available
          </h2>
          
          <p className="text-gray-500 text-center max-w-xs">
            We don&apos;t have any active offers right now. Check back later for
            exciting deals and discounts!
          </p>
        </div>
      </div>
      
      <BottomNav />
    </main>
  );
}
