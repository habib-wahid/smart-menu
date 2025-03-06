"use client";

import { useCart } from "../context/Context";

export default function Cart() {
    const {cart} = useCart();
    console.log("Cart", cart);
    return (
        <div>
            <h1>Cart</h1>
            <div>
                {
                    cart.map((item) => (
                        <div key={item.id} className="flex justify-between items-center m-5">
                            <div className="flex justify-center items-center w-24 h-8 bg-bilobaFlower rounded-3xl">
                                <div className="font-bold m-1">{item.name}</div>
                            </div>
                            <div className="text-neonMagenta text-2xl">
                                {item.price}
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>
    )
}