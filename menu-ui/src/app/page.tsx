import TopBar from './components/TopBar';
import CategoryList from '@/server-components/CategoryList';
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
   
          <TopBar />

          {/* Search Bar */}
          <div className="flex max-w-[calc(100vw-10rem)] bg-gray-500 rounded-3xl px-10 py-3 m-5">
            <Image src={searchIcon} alt="Search Icon" />
            <input type="text" className="bg-transparent outline-none w-full ml-5 font-bold" placeholder="Search"/>
          </div>

          {/* Foot Category */}

          {/* <div className='flex overflow-x-scroll no-scrollbar justify-between items-center m-5'>
            <div className='flex flex-col justify-center items-center min-w-20 min-h-20 bg-customGray rounded-3xl mr-3 flex-grow'>
              <Image src="http://localhost:8080/api/files/9aedda22-6920-4e1a-a07e-763e2beab889_meal.svg" width={35} height={35} alt="All Food Icon" />
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
          </div> */}

          <CategoryList />

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