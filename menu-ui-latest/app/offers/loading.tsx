export default function OffersLoading() {
  return (
    <main className="min-h-screen bg-white md:max-w-[390px] md:mx-auto md:shadow-xl">
      <div className="animate-pulse">
        {/* Header */}
        <div className="px-5 pt-8 pb-4">
          <div className="h-8 w-24 bg-gray-200 rounded-lg mb-2" />
          <div className="h-4 w-48 bg-gray-200 rounded" />
        </div>

        {/* Empty State Skeleton */}
        <div className="flex-1 flex flex-col items-center justify-center px-5 mt-32">
          <div className="w-24 h-24 bg-gray-200 rounded-full mb-6" />
          <div className="h-6 w-48 bg-gray-200 rounded mb-2" />
          <div className="h-4 w-64 bg-gray-200 rounded mb-1" />
          <div className="h-4 w-56 bg-gray-200 rounded" />
        </div>
      </div>
    </main>
  );
}
