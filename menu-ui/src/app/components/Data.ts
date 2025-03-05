import ChickenBurger from '@/../public/chickenBurger.svg';
import ChickenPizza from '@/../public/ChickenPizza.svg';
import GrillChicken from '@/../public/GrillChicken.svg';
import Platter from '@/../public/FoodPlater.svg';
import Burger from '@/../public/chickenBurger.svg';

export const MenuItem = () => {
    return [
        {
            id: 1,
            name: 'Chicken Burger',
            tag: 'Home',
            price: '$299',
            image: ChickenBurger,
            mainImage: Burger
        },
        {
            id: 2,
            name: 'Chicken Pizza',
            tag: '',
            price: '$199',
            image: ChickenPizza,
            mainImage: Burger,
        },
        {
            id: 3,
            name: 'Grill Chicken',
            tag: 'Home',
            price: '$299',
            image: GrillChicken,
            mainImage: Burger
        },
        {
            id: 4,
            name: 'Food Plater',
            tag: 'Special',
            price: '$299',
            image: Platter,
            mainImage: Burger
        },
        {
            id: 5,
            name: 'Food Plater',
            tag: 'Special',
            price: '$299',
            image: Platter,
            mainImage: Burger
        }
    ]
}