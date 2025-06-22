"use client";
import Image from "next/image";
import { useRouter } from "next/navigation";
// import { useCart } from "@/app/context/Context";
import BackArrow from "@/../public/BackArrow.svg";
import Star from "@/../public/Star.svg";
import PlusIcon from "@/../public/PlusIcon.svg";
import MinusIcon from "@/../public/RemoveIcon.svg";
import { MenuItem } from "@/Responses";
import { useEffect, useState } from "react";
import Footer from "@/app/components/Footer";
import { useOrder } from "@/app/context/ProductContext";

interface ItemDetails {
    item: MenuItem
}

export default function ProductDetailsClient({item}: ItemDetails) {
    const router = useRouter();
      const {order, addToOrder, removeFromOrder, addAddonsToItem} = useOrder();
      const [quantity, setQuantity] = useState(0);

        useEffect(() => {
          if (order.find((o) => o.itemId === item?.id)) {
            setQuantity(Number(order.find((i) => i.itemId === item?.id)?.quantity));
          } else {
            setQuantity(0);
          }
        }, [order]);
      

        const hanldeAddOrder = () => {
            addToOrder({
                itemId: item.id,
                quantity: 1,
                addons: []
            })
        }

        const handleAddAddons = (addon: any) => {
            addAddonsToItem(item.id, {
                addonId: addon.id, 
                quantity: 1 
            });
        }
        
    return (
        <>
          <div>
            <div className="bg-gradient-to-r from-[#A695EE] to-[#AB8EEE]">
              <Image
                src={BackArrow}
                alt="Back Arrow"
                className="ml-5 pt-5 cursor-pointer"
                onClick={() => router.back()}
              />
            </div>

            <div className="flex justify-center items-center w-full h-[40vh] bg-gradient-to-r from-[#A695EE] to-[#AB8EEE] relative rounded-bl-[50px]">
              <Image src={`${process.env.NEXT_PUBLIC_API_BASE_URL}/files/${item.filePath}`} alt="Chicken Burger" fill/>
            </div>
          </div>

          <div className="flex justify-between items-center m-5">
            <div className="flex justify-center items-center w-24 h-8 bg-bilobaFlower rounded-3xl">
              <Image src={Star} alt="Star Icon" />
              <div className="font-bold m-1">4.5</div>
            </div>
            <div className="text-neonMagenta text-2xl">{item.price}</div>
          </div>

          <div className="flex justify-between items-center mx-5">
            <div className="text-[25px] font-bold">{item.name}</div>
            <div className="flex justify-between items-center">
              <div>
                <Image
                  src={MinusIcon}
                  alt="Minus Icon"
                  className="cursor-pointer transition-transform duration-200 hover:scale-110"
                //   onClick={() => removeFromCart(item.id)}
                />
              </div>
              <div className="mx-2">{quantity}</div>
              <div>
                <Image
                  src={PlusIcon}
                  alt="Plus Icon"
                  className="cursor-pointer transition-transform duration-200 hover:scale-110"
                   onClick={hanldeAddOrder}
                />
              </div>
            </div>
          </div>

          <div className="mx-5 mt-2 text-lg">{item.description}</div>

          <div className="mx-5 mt-5 font-semibold text-[23px]">Add Ons</div>

          <div className="mx-5 mt-5 flex justify-center items-center">
            {item.addons.map((addOn) => (
              <div key={addOn.id}>
                <Image
                  src={`${process.env.NEXT_PUBLIC_API_BASE_URL}/files/${addOn.filePath}`}
                  width={70}
                  height={70}
                  alt="Add On"
                  className="cursor-pointer transition-transform duration-200 hover:scale-110"
                  onClick={() => handleAddAddons(addOn)}
                  
                />
              </div>
            ))}
          </div>
          <div className="mx-10 mt-auto text-center h-12 bg-pastelLavender rounded-3xl">
            <button
              className="w-full h-full font-bold text-2xl cursor-pointer transition-transform duration-200 hover:scale-110"
              onClick={() => router.push("/cart")}
            >
              Add to Cart
            </button>
          </div>
          <Footer />
        </>
    );
}