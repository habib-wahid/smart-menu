import TopBar from './components/TopBar';
import CategoryList from '@/server-components/CategoryList';
import PopularItemList from '@/server-components/PopularItemList';
import searchIcon from '@/../public/searchIcon.svg';
import Image from 'next/image';
import PromotionBox from './components/PromotionBox';
import Footer from './components/Footer';
import Category from '@/server-components/Category';



export default function Home() {
  return (
    <>
   
          <TopBar />

          {/* Search Bar */}
          <div className="flex max-w-[calc(100vw-10rem)] bg-gray-500 rounded-3xl px-10 py-3 m-5">
            <Image src={searchIcon} alt="Search Icon" />
            <input type="text" className="bg-transparent outline-none w-full ml-5 font-bold" placeholder="Search"/>
          </div>
          
            {/* <div className="w-full overflow-x-auto flex space-x-2 p-5">
            {[...Array(5)].map((_, index) => (
              <div
                key={index}
                className="min-w-[150px] h-10 bg-red-300 rounded-xl flex items-center justify-center text-white font-bold"
              >
                Box {index + 1}
              </div>
            ))}

           
          </div> */}

              <Category />

          
          
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