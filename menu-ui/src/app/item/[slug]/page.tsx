"use client"

import Footer from "@/app/components/Footer";
import Star from '../../../../public/Star.svg';
import Image from "next/image";
import { useParams } from "next/navigation";
import { useContext, useEffect, useState } from "react";
import { MenuItem } from "@/app/components/Data";
import PlusIcon from '@/../public/PlusIcon.svg';
import MinusIcon from '@/../public/RemoveIcon.svg';
import { CartContext, useCart } from "@/app/context/Context";
export default function ItemDetails () {

    const {addToCart} = useCart();
    const [item, setItem] = useState<any>();
    const [quantity, setQuantity] = useState(1);
    const params = useParams();

    useEffect(() => {
        if (params?.slug) { 
            setItem(MenuItem().find((item) => item.id === Number(params.slug)))};
    }, [params]);
    
    console.log("Item", item);

    const handleAddToCart = () => {
       console.log("Clicked");
         addToCart({
              id: item.id,
              name: item.name,
              price: item.price,
              quantity: quantity
         });
    }

    // useEffect(() => {
    //     console.log("Quantity", quantity);
    //     handleAddToCart();
    // }, [quantity]);

    
    return (
        <>
        {
            item && (
                <>
                <div className="flex justify-center items-center w-full h-[40vh] bg-gradient-to-r from-[#A695EE] to-[#AB8EEE] relative rounded-bl-[50px]">
                    <Image src={item.image} alt="Chicken Burger" fill/>
                </div>
                            
                <div className="flex justify-between items-center m-5">
                    <div className="flex justify-center items-center w-24 h-8 bg-bilobaFlower rounded-3xl">
                        <Image src={Star} alt="Star Icon" />
                        <div className="font-bold m-1">4.5</div>
                    </div>
                    <div className="text-neonMagenta text-2xl">
                        $12
                    </div>
                </div>

                <div className="flex justify-between items-center mx-5">
                    <div className="text-[20px] font-bold">Chicken Burger</div>
                    <div className="flex justify-between items-center">
                        <div>
                            <Image src={MinusIcon} alt="Minus Icon"
                            className="cursor-pointer transition-transform duration-200 hover:scale-110"
                            onClick={() => setQuantity(quantity - 1)} />
                        </div>
                        <div className="mx-2">
                            1
                        </div>
                        <div>
                            <Image src={PlusIcon} alt="Plus Icon"
                            className="cursor-pointer transition-transform duration-200 hover:scale-110"
                            onClick={handleAddToCart} />
                        </div>
                    </div>
                </div>

                <div className="mx-5 mt-2 font-semibold text-lg">
                    {item.description}
                </div>
                <Footer />
                </>
            )
        }
          
        </>
    )
}