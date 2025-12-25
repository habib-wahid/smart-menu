const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL

// Customer types
export interface Customer {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  phone: string;
}

// Order types
export interface CustomerOrderAddonSummary {
  addonId: number;
  addonName: string;
  quantity: number;
}

export interface CustomerOrderItemSummary {
  orderItemId: number;
  itemId: number;
  itemName?: string;
  quantity: number;
  orderAddons: CustomerOrderAddonSummary[];
}

export interface CustomerOrderSummary {
  orderId: number;
  userId: number;
  orderStatus: string;
  totalPrice: number;
  orderTime: string | number[]; // Can be ISO string or array [year, month, day, hour, minute, second, nanoseconds]
  orderItems: CustomerOrderItemSummary[];
}

export type OrderStatus = "PLACED" | "PROCESSING" | "SHIPPING" | "COMPLETED" | "CANCELED";

// Fetch customer by phone number
export async function getCustomerByPhone(phone: string): Promise<Customer | null> {
  try {
    const response = await fetch(`${API_BASE_URL}/customer/phone/${phone}`);
    const data = await response.json();
    
    if (response.ok && data.data) {
      return data.data;
    }
    return null;
  } catch (error) {
    console.error("Error fetching customer:", error);
    return null;
  }
}

// Fetch customer by ID
export async function getCustomerById(customerId: number): Promise<Customer | null> {
  try {
    const response = await fetch(`${API_BASE_URL}/customer/${customerId}`);
    const data = await response.json();
    
    if (response.ok && data.data) {
      return data.data;
    }
    return null;
  } catch (error) {
    console.error("Error fetching customer:", error);
    return null;
  }
}

// Fetch customer orders by status
export async function getCustomerOrders(
  customerId: number,
  orderStatus: OrderStatus,
  page: number = 0,
  size: number = 10
): Promise<CustomerOrderSummary[]> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/customers/${customerId}/orders?orderStatus=${orderStatus}&page=${page}&size=${size}`
    );
    const data = await response.json();
    
    if (response.ok && data.data) {
        console.log("Fetched orders:", data.data);
      return data.data;
    }
    return [];
  } catch (error) {
    console.error("Error fetching orders:", error);
    return [];
  }
}

// Fetch order details
export async function getOrderDetails(
  customerId: number,
  orderId: number
): Promise<CustomerOrderSummary | null> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/customers/${customerId}/orders/${orderId}`
    );
    const data = await response.json();
    
    if (response.ok && data.data) {
        console.log("Fetched order details:", data.data);
      return data.data;
    }
    return null;
  } catch (error) {
    console.error("Error fetching order details:", error);
    return null;
  }
}
