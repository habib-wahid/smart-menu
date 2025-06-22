
export type MenuItem = {
    id: number;
    name: string;
    description: string;
    price: number;
    filePath: string;
    fullFilePath: string;
    rating: number;
    addons: OrderAddon[];
}

export type OrderAddon = {
    id: number;
    name: string;
    description: string;
    price: number;
    filePath: string;
    fullFilePath: string;
    rating: number;
}

export type OrderResponse = {
    orderId: number;
    userId: number;
    orderStatus: string;
    totalPrice: number;
    isPaid: boolean;
    isServed: boolean;
    tableNo: number;
    orderTime: Date;
    updateTime: Date;
    deliveryTime: Date;
    orderItems: {
        orderItemId: number;
        orderId: number;
        itemId: number;
        itemName: string;
        itemUnitPrice: number;
        quantity: number;
        totalPrice: number;
        orderAddons: {
            orderAddonId: number;
            orderItemId: number;
            addonId: number;
            addonName: string;
            addonUnitPrice: number;
            quantity: number;
            totalPrice: number;
        }[];
    }[]
}