"use client";
import Footer from "../components/Footer";
// import { useCart } from "../context/Context";
import Image from "next/image";
import PlusIcon from "@/../public/PlusIcon.svg";
import MinusIcon from "@/../public/RemoveIcon.svg";
import ChickenPizza from "@/../public/ChickenPizza.svg";
import OrderCancel from "@/../public/Cancel.svg";
import { ItemType } from "../components/Data";
import { OrderItem, useOrder } from "../context/ProductContext";
import { OrderRequest } from "@/Request";
import { createOrder } from "@/services/OrderService";
import { useState } from "react";
import { OrderResponse } from "@/Responses";

export default function CartClient() {

  const {order, addToOrder, removeFromOrder, addAddonsToItem} = useOrder();
 const [orderResponse, setOrderResponse] = useState<OrderResponse |  null>(null);

  const handleSubmitOrder = async() => {
    try {
        const orderRequest = {
            userId: 1, 
            tableNumber: 1,
            orderItems: order.map(item => ({
                itemId: item.itemId,
                quantity: item.quantity,
                orderAddons: item.addons.map(addon => ({
                    addonId: addon.addonId,
                    quantity: addon.quantity
                }))
            }))
        } as OrderRequest;

        const response = await createOrder(orderRequest);
        setOrderResponse(response);
        if (response.orderId) {
            localStorage.removeItem("order");
            console.log("Order created successfully:", response);
        }
    } catch(error) {
        console.error("Error creating order:", error);
    }
  }
  const calculateTotal = (order: OrderItem[]) => {
    const total = order.reduce((acc, item) => {
      const itemTotal = item.quantity * item.price;
      const addonsTotal = item.addons.reduce((addonAcc, addon) => {
        return addonAcc + addon.quantity * addon.price;
      }, 0);
      return acc + itemTotal + addonsTotal;
    }, 0);

    return total;
  }

  return (
    <>
     {
        orderResponse != null && (
            <div className="p-4 space-y-4">
                <h2 className="text-xl font-bold">Order Placed successfully</h2>
                <h2 className="text-xl font-bold">Order Summary</h2>

                <div className="border p-4 rounded-lg shadow-md">
                    <p><strong>Order ID:</strong> {orderResponse.orderId}</p>
                    <p><strong>Table No:</strong> {orderResponse.tableNo}</p>
                    <p><strong>Total Price:</strong> ${orderResponse.totalPrice.toFixed(2)}</p>
                    <p><strong>Status:</strong> {orderResponse.orderStatus}</p>
                    <p><strong>Order Time:</strong> {new Date(orderResponse.orderTime).toLocaleString()}</p>
                </div>

                <div>
                    <h3 className="text-lg font-semibold mt-4 mb-2">Items:</h3>
                    {orderResponse.orderItems.map((item) => (
                    <div key={item.orderItemId} className="mb-4 p-4 border rounded-md bg-white shadow-sm">
                        <div className="font-medium">
                        🍽 {item.itemName} × {item.totalPrice.toFixed(2)}
                        </div>
                        <div className="ml-4 mt-2">
                        {item.orderAddons.length > 0 ? (
                            <div>
                            <p className="text-sm font-semibold">➕ Addons:</p>
                            <ul className="text-sm list-disc list-inside">
                                {item.orderAddons.map((addon) => (
                                <li key={addon.orderAddonId}>
                                    {addon.addonName} × {addon.totalPrice.toFixed(2)}
                                </li>
                                ))}
                            </ul>
                            </div>
                        ) : (
                            <p className="text-sm text-gray-500">No Addons</p>
                        )}
                        </div>
                    </div>
                    ))}
                </div>
            </div>
          
        )
    }

     { orderResponse === null && (
      <div className="mx-5 mt-5">
        <p className="font-bold text-2xl">
          {order.map((item) => item.quantity).reduce((a, b) => a + b, 0)} items
          in cart
        </p>

        <div className="max-h-80 overflow-y-auto no-scrollbar">
          {order.length > 0 &&
            order.map((item) => (
              <div className="flex justify-between items-center mt-5">
                <div className="flex justify-start items-center">
                  <div className="flex justify-center items-center bg-cartItemBackground rounded-3xl">
                    <Image
                      src={`${process.env.NEXT_PUBLIC_API_BASE_URL}/files/${item.filePath}`}
                      alt="Item Image"
                      width={104}
                      height={95}
                    />
                  </div>
                  <div className="flex flex-col justify-start items-start mx-2">
                    <div className="text-center">{item.name}</div>
                    <div className="text-neonMagenta ml text-center">
                      {" "}
                      {item.quantity * item.price}{" "}
                    </div>
                    <div className="flex justify-center items-center">
                      <div>
                        <Image
                          src={MinusIcon}
                          alt="Minus Icon"
                          className="cursor-pointer transition-transform duration-200 hover:scale-110"
                         // onClick={() => removeFromCart(item.id)}
                        />
                      </div>
                      <div className="mx-5 text-xl"> {item.quantity} </div>
                      <div>
                        <Image
                          src={PlusIcon}
                          alt="Plus Icon"
                          className="cursor-pointer transition-transform duration-200 hover:scale-110"
                        //  onClick={() => handleAddToCart(item)}
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
            ))}
        </div>

        <div className="text-xl font-bold mt-5">Apply Coupon Code</div>

        <div className="flex w-4/5 bg-customGray rounded-3xl px-10 py-3 mt-5">
          <input
            type="text"
            className="bg-transparent outline-none w-full text-black"
            placeholder="E.g. New10"
          />
        </div>

        <div className="flex justify-between items-center mt-5">
          <div className="font-bold text-xl">Total</div>
          <div className="text-neonMagenta text-xl">$ {calculateTotal(order)}</div>
        </div>

        <div>
          <div className="mx-10 mt-5 text-center h-12 bg-pastelLavender rounded-3xl">
            <button
              className="w-full h-full font-bold text-2xl cursor-pointer transition-transform duration-200 hover:scale-110"
              onClick={handleSubmitOrder}
            >
              Order Now
            </button>
          </div>
        </div>
      </div>
     )
    }
     

      <Footer />

    </>
  );
}
