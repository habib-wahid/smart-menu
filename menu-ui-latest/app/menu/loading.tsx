export default function MenuLoading() {
  return (
    <div className="min-h-screen bg-gray-200 flex justify-center">
      <div className="w-full max-w-[390px] bg-white min-h-screen relative pb-24">
        {/* Header Skeleton */}
        <header className="flex items-center justify-between px-5 pt-6 pb-4">
          <div className="h-8 w-20 bg-gray-200 rounded animate-pulse" />
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-gray-200 animate-pulse" />
            <div className="w-10 h-10 rounded-lg bg-gray-200 animate-pulse" />
          </div>
        </header>

        {/* Search Bar Skeleton */}
        <div className="px-5 mb-5">
          <div className="h-12 bg-gray-200 rounded-full animate-pulse" />
        </div>

        {/* Category Tabs Skeleton */}
        <div className="px-5 mb-6">
          <div className="flex gap-2">
            {[1, 2, 3, 4].map((i) => (
              <div
                key={i}
                className="h-10 w-20 bg-gray-200 rounded-full animate-pulse"
              />
            ))}
          </div>
        </div>

        {/* Popular Section Skeleton */}
        <section className="px-5 mb-6">
          <div className="h-6 w-24 bg-gray-200 rounded mb-4 animate-pulse" />
          <div className="grid grid-cols-2 gap-4">
            {[1, 2, 3, 4].map((i) => (
              <div
                key={i}
                className="bg-gray-100 rounded-2xl h-48 animate-pulse"
              />
            ))}
          </div>
        </section>

        {/* Promotion Banner Skeleton */}
        <section className="px-5 mb-6">
          <div className="h-6 w-28 bg-gray-200 rounded mb-4 animate-pulse" />
          <div className="h-24 bg-gray-200 rounded-2xl animate-pulse" />
        </section>

        {/* Special Section Skeleton */}
        <section className="px-5 mb-6">
          <div className="h-6 w-36 bg-gray-200 rounded mb-4 animate-pulse" />
          <div className="grid grid-cols-2 gap-4">
            {[1, 2].map((i) => (
              <div
                key={i}
                className="bg-gray-100 rounded-2xl h-48 animate-pulse"
              />
            ))}
          </div>
        </section>
      </div>
    </div>
  );
}
