import CartContent from "@/app/components/CartContent";

export default function CartPage() {
  return (
    <div className="min-h-screen bg-gray-200 flex justify-center">
      <div className="w-full max-w-[390px] bg-white min-h-screen relative">
        <CartContent />
      </div>
    </div>
  );
}
