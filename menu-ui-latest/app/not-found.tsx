import Link from "next/link";

export default function NotFound() {
  return (
    <div className="min-h-screen bg-gray-200 flex justify-center">
      <div className="w-full max-w-[390px] bg-white min-h-screen flex flex-col items-center justify-center p-6">
        <div className="w-24 h-24 bg-purple-100 rounded-full flex items-center justify-center mb-6">
          <span className="text-5xl">🍽️</span>
        </div>
        <h1 className="text-2xl font-bold text-gray-900 mb-2">Page Not Found</h1>
        <p className="text-gray-500 text-center mb-8">
          The page you&apos;re looking for doesn&apos;t exist.
        </p>
        <Link
          href="/menu"
          className="bg-purple-600 text-white px-6 py-3 rounded-full font-semibold hover:bg-purple-700 transition-colors"
        >
          Back to Menu
        </Link>
      </div>
    </div>
  );
}
