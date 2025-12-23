
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error: string[];
  timeStamp: string;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  imageUrl?: string;
  createdAt?: Date;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";

export async function fetchCategories(): Promise<Category[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/categories`, {
      next: { revalidate: 60 }, // Cache for 60 seconds
    });

    console.log("Fetching categories from:", `${API_BASE_URL}/categories`);

    if (!response.ok) {
      throw new Error("Failed to fetch categories");
    }
    const result: ApiResponse<Category[]> = await response.json();
    console.log("Fetched categories:", result.data);
    return result.data || [];
  } catch (error) {
    console.error("Error fetching categories:", error);
    return [];
  }
}