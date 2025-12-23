import { ApiResponse } from "./categoryService";

export interface Item {
  id: number;
  name: string;
  description: string;
  price: number;
  filePath: string;
  fileName: string;
  rating: number;
}

export interface Addon {
  id: number;
  name: string;
  description: string;
  price: number;
  filePath: string;
  fileName: string;
  rating: number;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";

export async function fetchItemById(id: string): Promise<Item | null> {
  try {
    const response = await fetch(`${API_BASE_URL}/item/${id}`, {
      next: { revalidate: 60 },
    });
    if (!response.ok) {
      throw new Error("Failed to fetch item");
    }
    const result: ApiResponse<Item> = await response.json();
    return result.data;
  } catch (error) {
    console.error("Error fetching item:", error);
    return null;
  }
}

export async function fetchAddons(): Promise<Addon[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/addon`, {
      next: { revalidate: 60 },
    });
    if (!response.ok) {
      throw new Error("Failed to fetch addons");
    }
    const result: Addon[] = await response.json();
    return result || [];
  } catch (error) {
    console.error("Error fetching addons:", error);
    return [];
  }
}
