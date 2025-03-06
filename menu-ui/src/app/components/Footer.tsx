'use client';

import HomeIcon from '../../../public/Home.svg';
import CartIcon from '../../../public/Cart.svg';
import ProfileIcon from '../../../public/Profile.svg';
import Image from 'next/image';
import { useRouter } from 'next/navigation';


export default function Footer() {

    const router = useRouter();

    return (
        <footer className="w-full h-12 py-2 px-5 bg-customGray flex items-center justify-evenly mt-auto">
            <Image className='cursor-pointer transition-transform hover:scale-105 active:scale-95'
             src={HomeIcon} 
             width={28} 
             height={28} 
             alt="Home Icon"
             onClick={() => router.push('/')}/>
             
            <Image className='cursor-pointer transition-transform hover:scale-105 active:scale-95' 
             src={CartIcon} alt="Cart Icon"
             onClick={() => router.push('/cart')}
             />
            <Image className='cursor-pointer transition-transform 
             hover:scale-105 active:scale-95' src={ProfileIcon} alt="Profile Icon" />
        </footer>
    )
}