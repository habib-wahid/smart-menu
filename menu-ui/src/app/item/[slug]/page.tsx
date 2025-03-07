"use client";

import Footer from "@/app/components/Footer";
import Star from "../../../../public/Star.svg";
import Image from "next/image";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { AddOns, MenuItem } from "@/app/components/Data";
import PlusIcon from "@/../public/PlusIcon.svg";
import MinusIcon from "@/../public/RemoveIcon.svg";
import BackArrow from "@/../public/BackArrow.svg";
import { useCart } from "@/app/context/Context";
import { useRouter } from "next/navigation";

export default function ItemDetails() {
  const router = useRouter();
  const { cart, addToCart, removeFromCart } = useCart();
  const [item, setItem] = useState<any>();
  const [quantity, setQuantity] = useState(0);
  const params = useParams();

  useEffect(() => {
    if (params?.slug) {
      setItem(MenuItem().find((item) => item.id === Number(params.slug)));
    }
  }, [params]);

  useEffect(() => {
    setQuantity(cart.find((i) => i.id === item?.id)?.quantity || 0);
  }, [item]);

  useEffect(() => {
    if (cart.find((i) => i.id === item?.id)) {
      setQuantity(Number(cart.find((i) => i.id === item?.id)?.quantity));
    } else {
      setQuantity(0);
    }
  }, [cart]);

  const handleAddToCart = () => {
    addToCart({
      id: item.id,
      name: item.name,
      price: item.price,
      quantity: 1,
      image: item.image,
    });
  };

  return (
    <>
      {item && (
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
              <Image src={item.image} alt="Chicken Burger" fill />
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
                  onClick={() => removeFromCart(item.id)}
                />
              </div>
              <div className="mx-2">{quantity}</div>
              <div>
                <Image
                  src={PlusIcon}
                  alt="Plus Icon"
                  className="cursor-pointer transition-transform duration-200 hover:scale-110"
                  onClick={handleAddToCart}
                />
              </div>
            </div>
          </div>

          <div className="mx-5 mt-2 text-lg">{item.description}</div>

          <div className="mx-5 mt-5 font-semibold text-[23px]">Add Ons</div>

          <div className="mx-5 mt-5 flex justify-between items-center">
            {AddOns().map((addOn) => (
              <div key={addOn.id}>
                <Image
                  src={addOn.image}
                  alt="Add On"
                  className="cursor-pointer transition-transform duration-200 hover:scale-110"
                  onClick={handleAddToCart}
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
      )}
    </>
  );
}
