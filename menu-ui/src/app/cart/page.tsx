
import { Suspense } from 'react';
import CartClient from './CartClient';
export default function Cart() {

  return (
   <Suspense fallback={<CartLoadingSkeleton />}>
    <CartClient />
   </Suspense>
  );
}

function CartLoadingSkeleton() {
  return (
    <div className="mx-5 mt-5">
      <div className="h-8 bg-gray-200 rounded-md w-32 mb-5 animate-pulse"></div>
      
      <div className="max-h-80 overflow-y-auto no-scrollbar">
        {[1, 2, 3].map((i) => (
          <div key={i} className="flex justify-between items-center mt-5">
            <div className="flex justify-start items-center">
              <div className="w-[104px] h-[95px] bg-gray-200 rounded-3xl animate-pulse"></div>
              <div className="flex flex-col justify-start items-start mx-2">
                <div className="w-24 h-4 bg-gray-200 rounded animate-pulse mb-2"></div>
                <div className="w-16 h-4 bg-gray-200 rounded animate-pulse mb-2"></div>
                <div className="flex justify-center items-center">
                  <div className="w-6 h-6 bg-gray-200 rounded animate-pulse"></div>
                  <div className="mx-5 w-8 h-6 bg-gray-200 rounded animate-pulse"></div>
                  <div className="w-6 h-6 bg-gray-200 rounded animate-pulse"></div>
                </div>
              </div>
            </div>
            <div className="w-6 h-6 bg-gray-200 rounded animate-pulse"></div>
          </div>
        ))}
      </div>
      
      <div className="w-32 h-6 bg-gray-200 rounded animate-pulse mt-5 mb-5"></div>
      <div className="w-4/5 h-12 bg-gray-200 rounded-3xl animate-pulse mb-5"></div>
      
      <div className="flex justify-between items-center mt-5 mb-5">
        <div className="w-16 h-6 bg-gray-200 rounded animate-pulse"></div>
        <div className="w-20 h-6 bg-gray-200 rounded animate-pulse"></div>
      </div>
      
      <div className="mx-10 mt-5 h-12 bg-gray-200 rounded-3xl animate-pulse"></div>
    </div>
  );
}