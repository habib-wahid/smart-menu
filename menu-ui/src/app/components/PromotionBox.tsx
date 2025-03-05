
import DiscountImage from '@/../public/discountIcon.svg';
import Image from 'next/image';

export default function PromotionBox() {
    return(
        <div className="flex justify-between items-center p-3 mt-1 w-10/12 h-20 bg-customMagenta rounded-3xl m-5">
            <div className="bg-customMagenta text-white text-xl font-bold">
               Extra 10% off on <br/>
               order above 499 
            </div>
            <div>
               <Image src = {DiscountImage} alt="Discount Icon" />
            </div>
        </div>
    )
}