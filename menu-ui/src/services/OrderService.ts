import { OrderRequest } from "@/Request";
import { OrderResponse } from "@/Responses";


export async function createOrder(orderRequest: OrderRequest): Promise<OrderResponse> {
    try {
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/order`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderRequest),
        });

        if (!response.ok) {
            throw new Error('Failed to create order');
        }

        const data : OrderResponse = await response.json();
        return data;
    } catch (error) {
        console.error('Error creating order:', error);
        throw error; 
    }
}