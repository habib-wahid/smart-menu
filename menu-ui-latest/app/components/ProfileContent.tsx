"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import {
  User,
  Phone,
  Package,
  Clock,
  CheckCircle,
  XCircle,
  Truck,
  ChefHat,
  ChevronRight,
  LogOut,
  RefreshCw,
} from "lucide-react";
import {
  type Customer,
  type CustomerOrderSummary,
  type OrderStatus,
  getCustomerByPhone,
  getCustomerOrders,
} from "@/services/profileService";

const ORDER_STATUS_CONFIG: Record<
  string,
  { label: string; icon: React.ReactNode; color: string; bgColor: string }
> = {
  PLACED: {
    label: "Placed",
    icon: <Package size={16} />,
    color: "text-blue-600",
    bgColor: "bg-blue-100",
  },
  PROCESSING: {
    label: "Processing",
    icon: <ChefHat size={16} />,
    color: "text-orange-600",
    bgColor: "bg-orange-100",
  },
  SHIPPING: {
    label: "Shipping",
    icon: <Truck size={16} />,
    color: "text-purple-600",
    bgColor: "bg-purple-100",
  },
  COMPLETED: {
    label: "Completed",
    icon: <CheckCircle size={16} />,
    color: "text-green-600",
    bgColor: "bg-green-100",
  },
  CANCELED: {
    label: "Canceled",
    icon: <XCircle size={16} />,
    color: "text-red-600",
    bgColor: "bg-red-100",
  },
};

const ORDER_TABS: { status: OrderStatus; label: string }[] = [
  { status: "PLACED", label: "Placed" },
  { status: "PROCESSING", label: "Processing" },
  { status: "COMPLETED", label: "Completed" },
  { status: "CANCELED", label: "Canceled" },
];

export default function ProfileContent() {
  const [phone, setPhone] = useState<string>("");
  const [inputPhone, setInputPhone] = useState<string>("");
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [orders, setOrders] = useState<CustomerOrderSummary[]>([]);
  const [activeTab, setActiveTab] = useState<OrderStatus>("PLACED");
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingOrders, setIsLoadingOrders] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Load saved phone from localStorage on mount
  useEffect(() => {
    const savedPhone = localStorage.getItem("customerPhone");
    if (savedPhone) {
      setPhone(savedPhone);
      setInputPhone(savedPhone);
    }
  }, []);

  // Fetch customer when phone changes
  useEffect(() => {
    if (phone) {
      fetchCustomer(phone);
    }
  }, [phone]);

  // Fetch orders when customer or tab changes
  useEffect(() => {
    if (customer) {
      fetchOrders(customer.id, activeTab);
    }
  }, [customer, activeTab]);

  const fetchCustomer = async (phoneNumber: string) => {
    setIsLoading(true);
    setError(null);
    
    const customerData = await getCustomerByPhone(phoneNumber);
    
    if (customerData) {
      setCustomer(customerData);
      localStorage.setItem("customerPhone", phoneNumber);
    } else {
      setError("No customer found with this phone number");
      setCustomer(null);
    }
    
    setIsLoading(false);
  };

  const fetchOrders = async (customerId: number, status: OrderStatus) => {
    setIsLoadingOrders(true);
    const ordersData = await getCustomerOrders(customerId, status);
    setOrders(ordersData);
    setIsLoadingOrders(false);
  };

  const handlePhoneSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (inputPhone.length >= 10) {
      setPhone(inputPhone);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("customerPhone");
    setPhone("");
    setInputPhone("");
    setCustomer(null);
    setOrders([]);
  };

  const formatDate = (dateInput: string | number[]) => {
    let date: Date;
    
    // Handle array format: [year, month, day, hour, minute, second, nanoseconds]
    if (Array.isArray(dateInput)) {
      const [year, month, day, hour, minute, second] = dateInput;
      // Month is 1-indexed from backend, but Date constructor expects 0-indexed
      date = new Date(year, month - 1, day, hour, minute, second);
    } else {
      date = new Date(dateInput);
    }
    
    return date.toLocaleDateString("en-IN", {
      day: "numeric",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  // Phone input screen
  if (!customer && !isLoading) {
    return (
      <div className="flex flex-col min-h-screen">
        <header className="px-5 pt-8 pb-4">
          <h1 className="text-2xl font-bold text-gray-900">My Profile</h1>
        </header>

        <div className="flex-1 px-5 flex flex-col items-center justify-center">
          <div className="w-20 h-20 bg-purple-100 rounded-full flex items-center justify-center mb-6">
            <User size={40} className="text-purple-600" />
          </div>

          <h2 className="text-xl font-bold text-gray-900 mb-2">Welcome!</h2>
          <p className="text-gray-500 text-center mb-8">
            Enter your phone number to view your profile and orders
          </p>

          <form onSubmit={handlePhoneSubmit} className="w-full max-w-sm">
            <div className="relative mb-4">
              <Phone
                size={20}
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
              />
              <input
                type="tel"
                placeholder="Enter phone number"
                value={inputPhone}
                onChange={(e) => setInputPhone(e.target.value)}
                className="w-full bg-gray-100 rounded-full py-4 pl-12 pr-5 text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500"
                maxLength={11}
              />
            </div>

            {error && (
              <p className="text-red-500 text-sm text-center mb-4">{error}</p>
            )}

            <button
              type="submit"
              disabled={inputPhone.length < 10}
              className="w-full py-4 bg-purple-600 text-white text-lg font-bold rounded-full hover:bg-purple-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Continue
            </button>
          </form>

          <Link
            href="/menu"
            className="mt-6 text-purple-600 font-medium hover:text-purple-700"
          >
            Browse Menu Instead
          </Link>
        </div>
      </div>
    );
  }

  // Loading screen
  if (isLoading) {
    return (
      <div className="flex flex-col min-h-screen items-center justify-center">
        <RefreshCw size={40} className="text-purple-600 animate-spin mb-4" />
        <p className="text-gray-500">Loading profile...</p>
      </div>
    );
  }

  // Profile screen
  return (
    <div className="flex flex-col min-h-screen pb-24">
      {/* Header */}
      <header className="px-5 pt-8 pb-4 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900">My Profile</h1>
        <button
          onClick={handleLogout}
          className="p-2 rounded-full hover:bg-gray-100 transition-colors"
          title="Logout"
        >
          <LogOut size={20} className="text-gray-500" />
        </button>
      </header>

      {/* Customer Info Card */}
      <div className="mx-5 p-5 bg-gradient-to-br from-purple-600 to-purple-800 rounded-2xl text-white mb-6">
        <div className="flex items-center gap-4">
          <div className="w-16 h-16 bg-white/20 rounded-full flex items-center justify-center">
            <User size={32} className="text-white" />
          </div>
          <div className="flex-1">
            <h2 className="text-xl font-bold">
              {customer?.firstName} {customer?.lastName}
            </h2>
            <p className="text-purple-200 text-sm">@{customer?.username}</p>
          </div>
        </div>
        <div className="mt-4 pt-4 border-t border-white/20 flex items-center gap-2">
          <Phone size={16} className="text-purple-200" />
          <span className="text-purple-100">{customer?.phone}</span>
        </div>
      </div>

      {/* Orders Section */}
      <div className="px-5">
        <h2 className="text-lg font-bold text-gray-900 mb-4">My Orders</h2>

        {/* Order Status Tabs */}
        <div className="flex gap-2 overflow-x-auto pb-2 mb-4 -mx-5 px-5">
          {ORDER_TABS.map((tab) => (
            <button
              key={tab.status}
              onClick={() => setActiveTab(tab.status)}
              className={`px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-colors ${
                activeTab === tab.status
                  ? "bg-purple-600 text-white"
                  : "bg-gray-100 text-gray-600 hover:bg-gray-200"
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Orders List */}
        {isLoadingOrders ? (
          <div className="flex items-center justify-center py-12">
            <RefreshCw size={24} className="text-purple-600 animate-spin" />
          </div>
        ) : orders.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-12">
            <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mb-4">
              <Package size={28} className="text-gray-400" />
            </div>
            <p className="text-gray-500">No {activeTab.toLowerCase()} orders</p>
          </div>
        ) : (
          <div className="space-y-4">
            {orders.map((order) => {
              const statusConfig = ORDER_STATUS_CONFIG[order.orderStatus] || ORDER_STATUS_CONFIG.PLACED;
              return (
                <div
                  key={order.orderId}
                  className="bg-white border border-gray-200 rounded-2xl p-4 hover:shadow-md transition-shadow"
                >
                  {/* Order Header */}
                  <div className="flex items-center justify-between mb-3">
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-bold text-gray-900">
                        Order #{order.orderId}
                      </span>
                      <span
                        className={`px-2 py-1 rounded-full text-xs font-medium flex items-center gap-1 ${statusConfig.bgColor} ${statusConfig.color}`}
                      >
                        {statusConfig.icon}
                        {statusConfig.label}
                      </span>
                    </div>
                    <ChevronRight size={20} className="text-gray-400" />
                  </div>

                  {/* Order Time */}
                  <div className="flex items-center gap-2 text-sm text-gray-500 mb-3">
                    <Clock size={14} />
                    <span>{formatDate(order.orderTime)}</span>
                  </div>

                  {/* Order Items Preview */}
                  <div className="text-sm text-gray-600 mb-3">
                    {order.orderItems.length} item(s)
                  </div>

                  {/* Order Total */}
                  <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                    <span className="text-sm text-gray-500">Total</span>
                    <span className="text-lg font-bold text-purple-600">
                      ₹{order.totalPrice}
                    </span>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
