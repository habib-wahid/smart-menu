'use client';

import HomeIcon from '../../../public/Home.svg';
import CartIcon from '../../../public/Cart.svg';
import ProfileIcon from '../../../public/Profile.svg';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { faHouse } from '@fortawesome/free-solid-svg-icons/faHouse';
import { faBoxOpen } from '@fortawesome/free-solid-svg-icons';
import { faShoppingCart } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCartPlus } from '@fortawesome/free-solid-svg-icons';
// import { useCart } from '../context/Context';


export default function Footer() {

   // const {cart} = useCart();
    const router = useRouter();

    return (
        <footer className="w-full h-12 py-2 px-5 bg-customGray flex items-center justify-evenly mt-auto">
            <Image className='cursor-pointer transition-transform hover:scale-105 active:scale-95'
             src={HomeIcon} 
             width={28} 
             height={28} 
             alt="Home Icon"
             onClick={() => router.push('/')}/>
             
             <div className='relative'>
                <FontAwesomeIcon icon={faCartPlus} className='text-xl cursor-pointer transition-transform hover:scale-105 active:scale-95' 
                            onClick={() => router.push('/cart')}
                            />

                <span className="absolute top-0 right-0 -mt-2 -mr-1 bg-red-500 text-white text-xs font-bold px-1 rounded-full">
                    {/* {cart.map((item) => item.quantity).reduce((a, b) => a + b, 0)} */}
                </span>
             </div>
           
        
            <Image 
                className="cursor-pointer transition-transform hover:scale-105 active:scale-95" 
                src={ProfileIcon} 
                alt="Profile Icon" 
            />

            <FontAwesomeIcon icon={faHouse} className='text-xl' />
            <FontAwesomeIcon icon={faBoxOpen} className='text-xl' />

            {/* Notification Badge */}
           
    
        </footer>
    )
}