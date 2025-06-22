
import { useRouter } from "next/navigation";
import Image from "next/image";
import PopularItem from "./PopularItem";

export default async function PopularItemList() {

   // const router = useRouter();
  const response = await fetch('http://localhost:8080/api/popular-item', {
    cache: "no-cache"
  });

  if (!response.ok) {
    throw new Error("Failed to fetch popular items");
  }

  const popularItems = await response.json();
  console.log("Popular items " , popularItems);

  return (
    <div className="h-80 overflow-y-auto no-scrollbar m-5">
                <div className="grid grid-cols-2 gap-6">
                    {
                        popularItems.map((item : any) => (
                            
                                <PopularItem item={item} key={item.id}/>
                            
                        ) )
                    }
                   
                </div>
            </div>
  );
}