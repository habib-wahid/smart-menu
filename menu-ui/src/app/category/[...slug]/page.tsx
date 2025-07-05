
import Footer from "@/app/components/Footer";
import TopBar from "@/app/components/TopBar";
import { MenuItem } from "@/Responses";
import PopularItem from "@/server-components/PopularItem";

async function getItemsByCategory(id: number) : Promise<MenuItem[]> {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/item/by-category/${id}`)

    if (!response.ok) {
        throw new Error('Failed to fetch item');
    }

    return response.json() as Promise<MenuItem[]>;
}


export default async function CategoryItems({
  params,
}: {
  params: Promise<{ slug: string[] }>;
}) {

    const { slug } = await params;
    console.log("Category slug: ", slug);
    const {categoryId, categoryName} = {
        categoryId: slug[0],
        categoryName: slug[1] || "All"
      };
    const itemsByCategory = await getItemsByCategory(Number(categoryId));
    console.log("Items in category: ", itemsByCategory);
  return (
    <>
      <TopBar />
        {

            itemsByCategory.length > 0 ? (
                <div className="h-max overflow-y-auto no-scrollbar m-5"> 
                    <div className="text-2xl font-bold mb-5">
                        <h2>{categoryName} Items</h2>
                    </div>
                 <div className="grid grid-cols-2 gap-6">
                    {
                        itemsByCategory.map((item : any) => (
                            
                                <PopularItem item={item} key={item.id}/>
                            
                        ) )
                    }
                                    
                    </div>
                </div> 
            ) : (
                <div>

              </div>
            )
        }

        <Footer />
    </>
  );
}