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

const API_BASE_URL = "http://localhost:8080/api";

export async function fetchPopularItems(): Promise<PopularItem[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/popular-item`);
    if (!response.ok) {
      throw new Error("Failed to fetch popular items");
    }
    const result: ApiResponse<PopularItem[]> = await response.json();
    console.log("Fetched popular items :", result.data);
    return result.data;
  } catch (error) {
    console.error("Error fetching popular items:", error);
    return [];
  }
}
