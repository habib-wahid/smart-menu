
'use client';
import profileImage from '@/../public/profileImage.svg';
import accountDetails from '@/../public/account-details.svg';
import searchIcon from '@/../public/searchIcon.svg';
import allFoorIcon from '@/../public/allFood.svg';
import Meal from '@/../public/meal.svg';
import Soups from '@/../public/soups.png';
import Dessert from '@/../public/desert.svg';
import Image from 'next/image';
import Item from './components/Item';
import PromotionBox from './components/PromotionBox';
import Footer from './components/Footer';



export default function Home() {
  return (
    <>
   
          <div className="flex justify-between items-center m-5">
            <h2 className="text-3xl font-bold">Menu</h2>
            <div className="flex items-center gap-4">
              <Image src={profileImage} alt="Profile Image" className="w-10 h-10 rounded-full" />
              <Image src={accountDetails} alt="Profile Image"/>
            </div>
          </div>

          {/* Search Bar */}
          <div className="flex max-w-[calc(100vw-10rem)] bg-gray-500 rounded-3xl px-10 py-3 m-5">
            <Image src={searchIcon} alt="Search Icon" />
            <input type="text" className="bg-transparent outline-none w-full ml-5 font-bold" placeholder="Search"/>
          </div>

          {/* Foot Category */}

          <div className='flex overflow-x-scroll no-scrollbar justify-between items-center m-5'>
            <div className='flex flex-col justify-center items-center min-w-20 min-h-20 bg-customGray rounded-3xl mr-3 flex-grow'>
              <Image src={allFoorIcon} alt="All Food Icon" />
              <div className='text-sm p-1'>
                All
              </div>
            </div>
            <div className='flex flex-col justify-center items-center min-w-20 min-h-20 bg-customGray rounded-3xl mr-3'>
              <Image src={Meal} alt="All Food Icon" />
              <div className='text-sm p-1'>
                Meal
              </div>
            </div>
            <div className='flex flex-col justify-center items-center min-w-20 min-h-20 bg-customGray rounded-3xl mr-3'>
              <Image src={Soups} alt="All Food Icon" />
              <div className='text-sm p-1'>
                Soups
              </div>
            </div>
            <div className='flex flex-col justify-center items-center min-w-20 min-h-20 bg-customGray rounded-3xl mr-3'>
              <Image src={Dessert} alt="All Food Icon" />
              <div className='text-sm p-1'>
                Desserts
              </div>
            </div>
          </div>

          {/* Food Items */}

          <div className='ml-5 text-2xl font-bold'>
            <h2>Popular</h2>
          </div>

          <Item />

          <div className='mx-5 text-2xl font-bold'>
            <h2>Promotion</h2>
          </div>

          <PromotionBox />
         
          <Footer />

         
          </>
  );
}