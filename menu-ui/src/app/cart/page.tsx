"use client";

import { useCart } from "../context/Context";

export default function Cart() {
    const {cart} = useCart();
    console.log("Cart", cart);
    return (
        <div className="mx-5 mt-5">
            <p className="font-bold text-2xl">{cart.map((item) => item.quantity).reduce((a, b) => a + b, 0)} items in cart</p>
        </div>
    )
}