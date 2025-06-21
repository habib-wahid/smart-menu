import { MenuItem } from "@/Responses";
import ProductDetailsClient from "./ProductDetailsClient";

async function getItem(id: number) : Promise<MenuItem> {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/item/${id}`, {
        next: {
            revalidate: 60, // Revalidate every 60 seconds
    }
})

    if (!response.ok) {
        throw new Error('Failed to fetch item');
    }

    return response.json() as Promise<MenuItem>;
}

export default async function Product({params} : {params: Promise<{slug: string}>}) {
    const productId = (await params).slug;
    const item = await getItem(Number(productId));

    console.log("Product ID: ", item);

    if (!item) {
        return <div>Item not found</div>;
    }

    return (
        <>
            <ProductDetailsClient item={item} />
        </>
    )
}