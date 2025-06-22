
export type OrderRequest = {
    userId: number;
    tableNumber: number;
    orderItems: {
        itemId: number;
        quantity: number;
        orderAddons: {
            addonId: number;
            quantity: number;
        }[];
    }[];
}