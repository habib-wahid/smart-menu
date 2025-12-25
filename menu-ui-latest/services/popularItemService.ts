import { ApiResponse } from "./categoryService";

export interface PopularItem {
  id: number;
  name: string;
  description: string;
  price: number;
  filePath: string;
  fileName: string;
  rating: number;
  totalRevenue: number;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export async function fetchPopularItems(): Promise<PopularItem[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/popular-item`, {
      next: { revalidate: 60 }, // Cache for 60 seconds
    });
    if (!response.ok) {
      throw new Error("Failed to fetch popular items");
    }
    const result: ApiResponse<PopularItem[]> = await response.json();
    return result.data || [];
  } catch (error) {
    console.error("Error fetching popular items:", error);
    return [];
  }
}
