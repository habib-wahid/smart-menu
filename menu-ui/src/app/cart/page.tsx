"use client";

import Footer from "../components/Footer";
import { useCart } from "../context/Context";
import Image from "next/image";
import PlusIcon from "@/../public/PlusIcon.svg";
import MinusIcon from "@/../public/RemoveIcon.svg";
import OrderCancel from "@/../public/Cancel.svg";
import { ItemType } from "../components/Data";

export default function Cart() {
  const { cart, addToCart, removeFromCart } = useCart();

  const handleAddToCart = (item: any) => {
    addToCart({
        id: item.id,
        name: item.name,
        price: 10,
        quantity: 1,
        image: item.image,
    });
  };


  return (
    <>
      <div className="mx-5 mt-5">
        <p className="font-bold text-2xl">
          {cart.map((item) => item.quantity).reduce((a, b) => a + b, 0)} items
          in cart
        </p>

        {cart.length > 0 && 
        cart.map((item) => (
            <div className="flex justify-between items-center mt-5">
            <div className="flex justify-start items-center">
              <div className="flex justify-center items-center bg-cartItemBackground rounded-3xl">
                <Image src={item.image} alt="Item Image" width={104} height={95} />
              </div>
              <div className="flex flex-col justify-start items-start mx-2">
                <div className="text-center">{item.name}</div>
                <div className="text-neonMagenta ml text-center"> {item.price} </div>
                <div className="flex justify-center items-center">
                    <div>
                        <Image
                        src={MinusIcon}
                        alt="Minus Icon"
                        className="cursor-pointer transition-transform duration-200 hover:scale-110"
                        onClick={() => removeFromCart(item.id)}
                        />
                    </div>
                    <div className="mx-5 text-xl"> {item.quantity} </div>
                    <div>
                        <Image
                        src={PlusIcon}
                        alt="Plus Icon"
                        className="cursor-pointer transition-transform duration-200 hover:scale-110"
                        onClick={() => handleAddToCart(item)}
                        />
                    </div>
                </div>
               
              </div>
            </div>
            <div className="mr-2">
                <Image
                    src={OrderCancel}
                    alt="Order Cancel Icon"
                    className="cursor-pointer transition-transform duration-200 hover:scale-110"
                    onClick={() => {
                    console.log("sdfdsf");
                    }}
                />
            </div>
            </div>
           
        ))
        }
      </div>

      <Footer />
    </>
  );
}
