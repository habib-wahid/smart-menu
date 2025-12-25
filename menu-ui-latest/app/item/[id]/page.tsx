import { ArrowLeft } from "lucide-react";
import Link from "next/link";
import { notFound } from "next/navigation";
import { fetchItemById, fetchAddons } from "@/services/itemService";
import ItemDetailContent from "@/app/components/ItemDetailContent";

// Force dynamic rendering
export const dynamic = "force-dynamic";

interface ItemPageProps {
  params: Promise<{ id: string }>;
}

// Server Component - fetches data on the server
export default async function ItemDetailPage({ params }: ItemPageProps) {
  const { id } = await params;

  // Fetch item and addons in parallel
  const [item, addons] = await Promise.all([
    fetchItemById(id),
    fetchAddons(),
  ]);

  if (!item) {
    notFound();
  }

  return (
    <div className="min-h-screen bg-gray-200 flex justify-center">
      <div className="w-full max-w-[390px] bg-white min-h-screen relative">
        {/* Back Button */}
        <Link
          href="/menu"
          className="absolute top-6 left-5 z-10 w-10 h-10 bg-white/90 backdrop-blur-sm rounded-full flex items-center justify-center shadow-md hover:bg-white transition-colors"
        >
          <ArrowLeft size={20} className="text-gray-700" />
        </Link>

        {/* Item Detail Content - Client Component */}
        <ItemDetailContent item={item} addons={addons} />
      </div>
    </div>  
  );
}
