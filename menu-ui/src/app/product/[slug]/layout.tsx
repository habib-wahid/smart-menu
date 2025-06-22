import { OrderProvider } from "@/app/context/ProductContext";

export default function ProductDetailsLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <OrderProvider>
             {children}
        </OrderProvider> 
    );
}