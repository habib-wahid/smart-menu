"use client";
import Image from 'next/image';
import { useRouter } from 'next/navigation';

export default function PopularItem({ item }: { item: any }) {
    const router = useRouter();
    return (
        <div className="flex flex-col justify-center items-center min-w-24 min-h-32 bg-customGray rounded-3xl cursor-pointer transition-transform 
                     hover:scale-105 active:scale-95"
                                 key={item.id} onClick={() => router.push(`/item/${item.id}`)}>
                                    <Image src={`http://localhost:8080/api/files/${item.filePath}`} width={75} height={75} alt="Desserts Icon" />
                                    <div className="text-sm font-bold p-1">{item.name}</div>
                                    <div className='text-sm text-green-900'> {item.price} </div>
        </div>
    )
}