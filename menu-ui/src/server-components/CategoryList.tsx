import Image from "next/image";

type Category = {
    id: number;
    name: string;
    description: string;
    imageUrl: string;
    createdAt: Date;
}


export default async function CategoryList() {
   const categories = await fetch('http://localhost:8080/api/category', {
        cache: "no-cache"});

        if (!categories.ok) {
            throw new Error("Failed to fetch categories");
        }

    const categoriesData = await categories.json();

    return (
        <>
            {categoriesData.map((category: any) => (
                <div key={category.id}
                className="flex flex-col justify-center items-center min-w-20 min-h-20 bg-customGray rounded-3xl mr-3 flex-shrink-0">
                    <Image
                        src={category.imageUrl}
                        alt={category.name}
                        width={40}
                        height={40} />
                    <div className="text-sm p-1">{category.name}</div>
                </div>
            ))}
        </>
    );
    }