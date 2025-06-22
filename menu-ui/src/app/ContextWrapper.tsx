import { OrderProvider } from "./context/ProductContext";


export default function ContextWrapper({
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