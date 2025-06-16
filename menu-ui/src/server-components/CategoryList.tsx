import Image from "next/image";

type Category = {
    id: number;
    name: string;
    description: string;
    imageUrl: string;
    createdAt: Date;
}
async function fetchCategories(): Promise<Category[]> {
    const response = await fetch("http://localhost:8080/api/category", {
        cache: "no-cache",
    })


    if (!response.ok) {
        throw new Error("Failed to fetch categories");
    }

    const data = response.json().then((data) => {
        // Transform the data if necessary
        return data.map((category: any) => ({
            id: category.id,
            name: category.name,
            description: category.description,
            imageUrl: category.imageUrl,
            createdAt: new Date(category.createdAt),
        }));
    });

    console.log("Data fetched from server:", data);


    return data;
}

export default async function CategoryList() {
   const categories = await fetch('http://localhost:8080/api/category', {
        cache: "no-cache",});
    
    const categoriesData = await categories.json();

    console.log("Categories Data:", categoriesData);
    return (
        <div className="flex overflow-x-scroll no-scrollbar justify-between items-center m-5">
            {categoriesData.map((category: any) => (
                <div
                key={category.id}
                className="flex flex-col justify-center items-center min-w-20 min-h-20 bg-customGray rounded-3xl mr-3"
                >
                <Image
                    src={category.imageUrl}
                    alt={category.name}
                    width={40}
                    height={40} />
                <div className="text-sm p-1">{category.name}</div>
                </div>
            ))}

        
        </div>
    );
    }