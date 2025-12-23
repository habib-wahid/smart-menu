"use client";

import { useState } from "react";
import Link from "next/link";
import { ShoppingCart, CheckCircle, XCircle } from "lucide-react";
import CartItem from "./CartItem";
import { useCart } from "@/app/context/CartContext";

export default function CartContent() {
  const { 
    items, 
    updateQuantity, 
    removeFromCart,
    updateAddonQuantity,
    removeAddon,
    totalItems, 
    totalPrice,
    phone,
    tableNumber,
    discountCode,
    setPhone,
    setTableNumber,
    setDiscountCode,
    placeOrder
  } = useCart();
  
  const [isOrdering, setIsOrdering] = useState(false);
  const [orderResult, setOrderResult] = useState<{ success: boolean; message: string } | null>(null);

  const handleCheckout = async () => {
    setIsOrdering(true);
    setOrderResult(null);
    
    const result = await placeOrder();
    setOrderResult(result);
    setIsOrdering(false);
  };

  return (
    <div className="flex flex-col min-h-screen">
      {/* Header */}
      <header className="px-5 pt-8 pb-4">
        <h1 className="text-2xl font-bold text-gray-900">
          {totalItems > 0 
            ? `${totalItems} ${totalItems === 1 ? "item" : "items"} in cart`
            : "Your Cart"
          }
        </h1>
      </header>

      {/* Order Result Message */}
      {orderResult && (
        <div className={`mx-5 mb-4 p-4 rounded-xl flex items-center gap-3 ${
          orderResult.success 
            ? "bg-green-100 text-green-800" 
            : "bg-red-100 text-red-800"
        }`}>
          {orderResult.success ? (
            <CheckCircle size={20} className="text-green-600" />
          ) : (
            <XCircle size={20} className="text-red-600" />
          )}
          <span className="font-medium">{orderResult.message}</span>
        </div>
      )}

      {/* Cart Items List */}
      <div className="flex-1 px-5">
        {items.length === 0 && !orderResult?.success ? (
          <div className="flex flex-col items-center justify-center py-16">
            <div className="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mb-4">
              <ShoppingCart size={32} className="text-gray-400" />
            </div>
            <p className="text-gray-500 text-lg mb-2">No items selected</p>
            <p className="text-gray-400 text-sm mb-4">Add items from the menu to get started</p>
            <Link
              href="/menu"
              className="px-6 py-2 bg-purple-600 text-white font-semibold rounded-full hover:bg-purple-700 transition-colors"
            >
              Browse Menu
            </Link>
          </div>
        ) : orderResult?.success ? (
          <div className="flex flex-col items-center justify-center py-16">
            <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mb-4">
              <CheckCircle size={40} className="text-green-600" />
            </div>
            <p className="text-gray-800 text-xl font-bold mb-2">Order Placed!</p>
            <p className="text-gray-500 text-sm mb-4 text-center">
              Your order has been placed successfully.<br />
              We&apos;ll prepare it shortly.
            </p>
            <Link
              href="/menu"
              className="px-6 py-2 bg-purple-600 text-white font-semibold rounded-full hover:bg-purple-700 transition-colors"
            >
              Order More
            </Link>
          </div>
        ) : (
          <>
            <div className="divide-y divide-gray-100">
              {items.map((item, index) => (
                <CartItem
                  key={`${item.id}-${index}`}
                  item={item}
                  itemIndex={index}
                  onQuantityChange={updateQuantity}
                  onRemove={removeFromCart}
                  onAddonQuantityChange={updateAddonQuantity}
                  onAddonRemove={removeAddon}
                />
              ))}
            </div>

            {/* Order Details Section */}
            <div className="mt-8 space-y-4">
              <h2 className="text-base font-bold text-gray-900">Order Details</h2>
              
              {/* Phone Number */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Phone Number *
                </label>
                <input
                  type="tel"
                  placeholder="Enter 10-11 digit phone number"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  className="w-full bg-gray-100 rounded-full py-3 px-5 text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500"
                  maxLength={11}
                />
              </div>
              
              {/* Table Number */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Table Number *
                </label>
                <input
                  type="number"
                  placeholder="Enter your table number"
                  value={tableNumber || ""}
                  onChange={(e) => setTableNumber(parseInt(e.target.value) || 0)}
                  className="w-full bg-gray-100 rounded-full py-3 px-5 text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500"
                  min={1}
                />
              </div>
              
              {/* Coupon Code */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Coupon Code (Optional)
                </label>
                <input
                  type="text"
                  placeholder='E.g. "NEW10"'
                  value={discountCode}
                  onChange={(e) => setDiscountCode(e.target.value)}
                  className="w-full bg-gray-100 rounded-full py-3 px-5 text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
              </div>
            </div>

            {/* Total Section */}
            <div className="flex items-center justify-between mt-8 pt-4 border-t border-gray-200">
              <span className="text-lg font-semibold text-gray-900">Total :</span>
              <span className="text-2xl font-bold text-purple-600">₹{totalPrice}</span>
            </div>
          </>
        )}
      </div>

      {/* Bottom Actions */}
      {items.length > 0 && !orderResult?.success && (
        <div className="px-5 pb-8 pt-6 mt-auto">
          {/* Checkout Button */}
          <button 
            onClick={handleCheckout}
            disabled={isOrdering}
            className="w-full py-4 bg-purple-600 text-white text-lg font-bold rounded-full hover:bg-purple-700 transition-colors active:scale-[0.98] disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isOrdering ? "Placing Order..." : "Place Order"}
          </button>

          {/* Back to Menu Link */}
          <Link
            href="/menu"
            className="block text-center mt-4 text-gray-700 font-medium hover:text-gray-900 transition-colors"
          >
            Back to Menu
          </Link>
        </div>
      )}
    </div>
  );
}
