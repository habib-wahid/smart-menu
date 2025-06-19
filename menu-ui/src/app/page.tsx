import TopBar from './components/TopBar';
import CategoryList from '@/server-components/CategoryList';
import PopularItemList from '@/server-components/PopularItemList';
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
          
         <div className="flex overflow-x-scroll no-scrollbar justify-between items-center m-5">
          <CategoryList />
        </div>
          
          <div className='ml-5 text-2xl font-bold'>
            <h2>Popular</h2>
          </div>
          <PopularItemList />

          {/* Food Items */}


          {/* <Item /> */}

          <div className='mx-5 text-2xl font-bold'>
            <h2>Promotion</h2>
          </div>

          <PromotionBox />
         
          <Footer />

         
          </>
  );
}