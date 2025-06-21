
export type MenuItem = {
    id: number;
    name: string;
    description: string;
    price: number;
    filePath: string;
    fullFilePath: string;
    rating: number;
    addons: {
        id: number;
        name: string;
        description: string;
        price: number;
        filePath: string;
        fullFilePath: string;
        rating: number;
    }[];
}