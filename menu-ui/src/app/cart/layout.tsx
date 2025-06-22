import { OrderProvider } from "../context/ProductContext";


export default function CartLayout( {
    children,
} :  {
    children: React.ReactNode;
}) {
    return (
    <OrderProvider>
        {children} 
    </OrderProvider>)
}
