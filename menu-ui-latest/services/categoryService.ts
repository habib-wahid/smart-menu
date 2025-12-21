
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

const API_BASE_URL = "http://localhost:8080/api"

export default async function fetchCategories(): Promise<Category[]> {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`);
        if (!response.ok) {
            throw new Error("Failed to fetch categories");
        }
        const result: ApiResponse<Category[]> = await response.json();
        console.log("Fetched categories:", result.data);
        return result.data;
    } catch (error) {
        throw error;
    }
}