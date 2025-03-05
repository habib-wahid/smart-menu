
"use client";

import Image from 'next/image';
import { MenuItem } from './Data';
import { useRouter } from 'next/navigation';


export default function Item() {
    const router = useRouter();
    
    return (
        <div className="h-80 overflow-y-auto no-scrollbar m-5">
            <div className="grid grid-cols-2 gap-6">
                {
                    MenuItem().map((item) => (
                        <div className="flex flex-col justify-center items-center min-w-24 min-h-32 bg-customGray rounded-3xl cursor-pointer transition-transform 
             hover:scale-105 active:scale-95"
                         key={item.id} onClick={() => router.push(`/item/${item.id}`)}>
                            <Image src={item.image} alt="Desserts Icon" />
                            <div className="text-sm font-bold p-1">{item.name}</div>
                            <div className='text-sm text-green-900'> {item.price} </div>
                        </div>
                    ) )
                }
               
            </div>
        </div>
    )
}
