export default function ProfileLoading() {
  return (
    <main className="min-h-screen bg-white md:max-w-[390px] md:mx-auto md:shadow-xl">
      <div className="animate-pulse">
        {/* Header */}
        <div className="px-5 pt-8 pb-4 flex items-center justify-between">
          <div className="h-8 w-32 bg-gray-200 rounded-lg" />
          <div className="w-10 h-10 bg-gray-200 rounded-full" />
        </div>

        {/* Profile Card */}
        <div className="mx-5 p-5 bg-gray-200 rounded-2xl mb-6">
          <div className="flex items-center gap-4">
            <div className="w-16 h-16 bg-gray-300 rounded-full" />
            <div className="flex-1 space-y-2">
              <div className="h-6 w-32 bg-gray-300 rounded" />
              <div className="h-4 w-24 bg-gray-300 rounded" />
            </div>
          </div>
          <div className="mt-4 pt-4 border-t border-gray-300">
            <div className="h-4 w-28 bg-gray-300 rounded" />
          </div>
        </div>

        {/* Orders Section */}
        <div className="px-5">
          <div className="h-6 w-28 bg-gray-200 rounded mb-4" />

          {/* Tabs */}
          <div className="flex gap-2 mb-4">
            {[1, 2, 3, 4].map((i) => (
              <div key={i} className="h-10 w-24 bg-gray-200 rounded-full" />
            ))}
          </div>

          {/* Order Cards */}
          <div className="space-y-4">
            {[1, 2, 3].map((i) => (
              <div key={i} className="bg-gray-100 rounded-2xl p-4">
                <div className="flex items-center justify-between mb-3">
                  <div className="h-5 w-28 bg-gray-200 rounded" />
                  <div className="h-5 w-20 bg-gray-200 rounded-full" />
                </div>
                <div className="h-4 w-36 bg-gray-200 rounded mb-3" />
                <div className="h-4 w-20 bg-gray-200 rounded mb-3" />
                <div className="flex items-center justify-between pt-3 border-t border-gray-200">
                  <div className="h-4 w-12 bg-gray-200 rounded" />
                  <div className="h-6 w-16 bg-gray-200 rounded" />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </main>
  );
}
