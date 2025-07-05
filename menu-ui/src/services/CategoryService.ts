import { CategoryResponse } from "@/Responses";


export async function getCategoryList(): Promise<CategoryResponse[]> {
    try {
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/category`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        
        });

        if (!response.ok) {
            throw new Error('Failed to create order');
        }

        const data : CategoryResponse[] = await response.json();
        return data;
    } catch (error) {
        console.error('Error creating order:', error);
        throw error; 
    }
}