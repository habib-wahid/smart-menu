export default function ItemLoading() {
  return (
    <div className="min-h-screen bg-gray-200 flex justify-center">
      <div className="w-full max-w-[390px] bg-white min-h-screen relative">
        {/* Back Button Skeleton */}
        <div className="absolute top-6 left-5 z-10 w-10 h-10 bg-gray-200 rounded-full animate-pulse" />

        {/* Image Section Skeleton */}
        <div className="h-72 bg-gradient-to-b from-gray-200 to-gray-300 rounded-b-[40px] animate-pulse" />

        {/* Content Section */}
        <div className="px-5 pt-6 pb-24">
          {/* Rating and Price Row Skeleton */}
          <div className="flex items-center justify-between mb-3">
            <div className="h-8 w-20 bg-gray-200 rounded-full animate-pulse" />
            <div className="h-8 w-24 bg-gray-200 rounded animate-pulse" />
          </div>

          {/* Product Name Skeleton */}
          <div className="h-8 w-48 bg-gray-200 rounded mb-4 animate-pulse" />

          {/* Quantity Selector Skeleton */}
          <div className="flex items-center gap-4 mb-6">
            <div className="h-6 w-20 bg-gray-200 rounded animate-pulse" />
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-gray-200 animate-pulse" />
              <div className="w-8 h-8 bg-gray-200 rounded animate-pulse" />
              <div className="w-10 h-10 rounded-full bg-gray-200 animate-pulse" />
            </div>
          </div>

          {/* Description Skeleton */}
          <div className="mb-6">
            <div className="h-6 w-28 bg-gray-200 rounded mb-2 animate-pulse" />
            <div className="space-y-2">
              <div className="h-4 w-full bg-gray-200 rounded animate-pulse" />
              <div className="h-4 w-3/4 bg-gray-200 rounded animate-pulse" />
            </div>
          </div>

          {/* Add Ons Section Skeleton */}
          <div className="mb-6">
            <div className="h-6 w-24 bg-gray-200 rounded mb-4 animate-pulse" />
            <div className="flex gap-4">
              {[1, 2, 3].map((i) => (
                <div key={i} className="flex-shrink-0">
                  <div className="w-24 h-24 rounded-2xl bg-gray-200 animate-pulse" />
                  <div className="h-4 w-16 bg-gray-200 rounded mt-2 mx-auto animate-pulse" />
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Bottom CTA Skeleton */}
        <div className="fixed bottom-0 left-0 right-0 p-5 bg-white border-t border-gray-100 md:max-w-[390px] md:left-1/2 md:-translate-x-1/2">
          <div className="h-14 bg-gray-200 rounded-full animate-pulse" />
        </div>
      </div>
    </div>
  );
}
